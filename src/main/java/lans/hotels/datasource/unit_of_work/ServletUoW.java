package lans.hotels.datasource.unit_of_work;

import lans.hotels.datasource.exceptions.IdentityMapException;
import lans.hotels.datasource.exceptions.MapperNotFoundException;
import lans.hotels.datasource.exceptions.UnitOfWorkException;
import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.facade.*;
import lans.hotels.datasource.identity_maps.IntegerIdentityMapRegistry;
import lans.hotels.domain.AbstractDomainObject;

import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.locks.ReentrantLock;

public class ServletUoW implements IUnitOfWork {
    // TODO: do not treat identity maps as a session cache - clear them every request (scope identity maps to request)
    public IIdentityMapRegistry identityMaps;
    private static final String attributeName = "UnitOfWork";
    private static final HashMap<Thread, ServletUoW> activeUnitsOfWork = new HashMap<>();
    private static final ReentrantLock reentrantLock = new ReentrantLock();

    private HashSet<AbstractDomainObject> newObjects;
    private HashSet<AbstractDomainObject> dirtyObjects;
    private HashSet<AbstractDomainObject> removedObjects;
    private HashSet<AbstractDomainObject> cleanObjects;

    public ServletUoW(IIdentityMapRegistry identityMaps) {
        this.identityMaps = identityMaps;
        this.newObjects = new HashSet<>();
        this.dirtyObjects = new HashSet<>();
        this.removedObjects = new HashSet<>();
        this.cleanObjects = new HashSet<>();
    }

    synchronized public static ServletUoW getCurrent() throws UnitOfWorkException {
        ServletUoW current = activeUnitsOfWork.get(Thread.currentThread());
        if (current == null) {
            throw new UnitOfWorkException("Unit of Work was not initialised for this thread. UnitOfWork.handleSession() must be called in order to associate a UoW instance with a thread");
        }
        return current;
    }

    public static void handleSession(HttpSession session, IntegerIdentityMapRegistry freshIdentityMap) {
        reentrantLock.lock();
        ServletUoW uow = (ServletUoW) session.getAttribute(attributeName);
        if (uow == null) {
            uow = ServletUoW.newActiveUoW(freshIdentityMap);
        } else {
            for(Object identityMap: uow.identityMaps.getAll()) {
                ((IIdentityMap<?>) identityMap).clear();
            }
        }
        session.setAttribute(attributeName, uow);
        activeUnitsOfWork.put(Thread.currentThread(), uow);
        reentrantLock.unlock();
    }

    synchronized public static IntegerIdentityMapRegistry getActiveIdentityMaps(HttpSession session) {
        ServletUoW uow = (ServletUoW) session.getAttribute(attributeName);
        if (uow == null) return null;
        return (IntegerIdentityMapRegistry) uow.identityMaps; // TODO: ??? #bug
    }

    private static ServletUoW newActiveUoW(IntegerIdentityMapRegistry identityMaps) {
        return new ServletUoW(identityMaps);
    }

    public static void removeCurrent() {
        // TODO: exception handling?
        activeUnitsOfWork.remove(Thread.currentThread());
    }

    @Override
    public void registerNew(AbstractDomainObject obj) throws UoWException {
        if (obj.hasId()) throw new UoWException("attempting to register an object that already has an ID as 'new'.");
        if (dirtyObjects.contains(obj)) throw new UoWException("attempting to register a 'dirty' object as 'new'.");
        if (removedObjects.contains(obj)) throw new UoWException("attempting to register a 'removed' object as 'new'.");
        if (newObjects.contains(obj)) throw new UoWException("attempting to register a 'new' multiple times.");

        newObjects.add(obj);
    }

    @Override
    public <DomainObject extends AbstractDomainObject> void registerDirty(DomainObject obj) throws UoWException {
        if (!obj.hasId()) throw new UoWException("attempting to register an object with no ID as 'dirty'.");
        if (removedObjects.contains(obj)) throw new UoWException("attempting to register a 'removed' object as 'dirty'.");
        if (newObjects.contains(obj)) throw new UoWException("attempting to register a 'new' as 'dirty'.");
        cleanObjects.remove(obj);
        if (!dirtyObjects.contains(obj)) dirtyObjects.add(obj);
    }

    @Override
    public <DomainObject extends AbstractDomainObject> void registerRemoved(DomainObject obj) throws UoWException {
        if (!obj.hasId()) throw new UoWException("attempting to register an object with no ID as 'removed'.");
        dirtyObjects.remove(obj);
        removedObjects.add(obj);
        if (identityMaps.get(obj.getClass()).getById(obj.getId()) != null) identityMaps.get(obj.getClass()).remove(obj.getId());
    }

    @Override
    public <DomainObject extends AbstractDomainObject> void registerClean(DomainObject obj) throws UoWException {
        if (!obj.hasId()) throw new UoWException("attempting to register an object with no ID as 'clean'.");
        if (dirtyObjects.contains(obj)) throw new UoWException("attempting to register a 'dirty' object as 'clean'.");
        if (removedObjects.contains(obj)) throw new UoWException("attempting to register a 'removed' as 'clean'.");
        try {
            identityMaps.get(obj.getClass()).add(obj);
        } catch (IdentityMapException e) {
            System.err.println("ERROR UoW.registerClean(): " + e.getMessage());
            throw new UoWException(e.getMessage());
        }
    }

    @Override
    public void commit(Connection connection, IMapperRegistry mappers) throws Exception {
        try {
            ArrayList<AbstractDomainObject> newObjectsList = new ArrayList<>(newObjects);
            newObjectsList.sort(Comparator.naturalOrder());
            newObjectsList.forEach(obj -> {
                try {
                    mappers.getMapper(obj.getClass()).insert(obj);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            dirtyObjects.forEach(obj -> {
                try {
                    mappers.getMapper(obj.getClass()).update(obj);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            removedObjects.forEach(obj -> {
                try {
                    mappers.getMapper(obj.getClass()).delete(obj.getId());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                identityMaps.get(obj.getClass()).remove(obj.getId());
            });

            // Commit all statements
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            System.err.println("UoW Error: failed to commit");
            throw e;
        } finally {
            for(IIdentityMap<?> identityMap: identityMaps.getAll()) {
                identityMap.clear();
            }
            newObjects.clear();
            dirtyObjects.clear();
            removedObjects.clear();
            cleanObjects.clear();

            // Close the connection
            System.out.println("closing connection");
            connection.close();
        }
    }
}
