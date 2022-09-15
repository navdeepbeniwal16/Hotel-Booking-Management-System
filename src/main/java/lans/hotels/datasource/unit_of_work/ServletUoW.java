package lans.hotels.datasource.unit_of_work;

import lans.hotels.datasource.exceptions.IdentityMapException;
import lans.hotels.datasource.exceptions.UnitOfWorkException;
import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.facade.IIdentityMapRegistry;
import lans.hotels.datasource.facade.IMapperRegistry;
import lans.hotels.datasource.facade.IUnitOfWork;
import lans.hotels.datasource.identity_maps.IntegerIdentityMapRegistry;
import lans.hotels.domain.AbstractDomainObject;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.locks.ReentrantLock;

public class ServletUoW implements IUnitOfWork<Integer> {
    public IIdentityMapRegistry identityMaps;
    private static final String attributeName = "UnitOfWork";
    private static HashMap<Thread, ServletUoW> activeUnitsOfWork = new HashMap<>();
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
            session.setAttribute(attributeName, uow);
            System.out.println("UoW created");
        } else {
            System.out.println("Using existing UoW");
        }
        activeUnitsOfWork.put(Thread.currentThread(), uow);
        reentrantLock.unlock();
    }

    synchronized public static IntegerIdentityMapRegistry getActiveIdentityMaps(HttpSession session) {
        ServletUoW uow = (ServletUoW) session.getAttribute(attributeName);
        if (uow == null) return null;
        return (IntegerIdentityMapRegistry) uow.identityMaps;
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

        try {
            identityMaps.get(obj.getClass()).add(obj);
        } catch (IdentityMapException e) {
            throw new UoWException(e.getMessage());
        }
        newObjects.add(obj);
    }

    @Override
    public void registerDirty(AbstractDomainObject obj) throws UoWException {
        if (!obj.hasId()) throw new UoWException("attempting to register an object with no ID as 'dirty'.");
        if (removedObjects.contains(obj)) throw new UoWException("attempting to register a 'removed' object as 'dirty'.");
        if (newObjects.contains(obj)) throw new UoWException("attempting to register a 'new' as 'dirty'.");
        cleanObjects.remove(obj);
        if (!dirtyObjects.contains(obj)) dirtyObjects.add(obj);
    }

    @Override
    public void registerRemoved(AbstractDomainObject obj) throws UoWException {
        if (!obj.hasId()) throw new UoWException("attempting to register an object with no ID as 'removed'.");
        dirtyObjects.remove(obj);
        removedObjects.add(obj);
        if (identityMaps.get(obj.getClass()).getById(obj.getId()) != null) identityMaps.get(obj.getClass()).remove(obj.getId());
    }

    @Override
    public void registerClean(AbstractDomainObject obj) throws UoWException {
        if (!obj.hasId()) throw new UoWException("attempting to register an object with no ID as 'clean'.");
        if (dirtyObjects.contains(obj)) throw new UoWException("attempting to register a 'dirty' object as 'clean'.");
        if (removedObjects.contains(obj)) throw new UoWException("attempting to register a 'removed' as 'clean'.");
        try {
            identityMaps.get(obj.getClass()).add(obj);
        } catch (IdentityMapException e) {
            throw new UoWException(e.getMessage());
        }
    }

    @Override
    public void commit(IMapperRegistry<Integer> mappers) {
        newObjects.forEach(obj -> mappers.getMapper(obj.getClass()).create(obj));
        dirtyObjects.forEach(obj -> mappers.getMapper(obj.getClass()).update(obj));
        removedObjects.forEach(obj -> mappers.getMapper(obj.getClass()).delete((Integer) obj.getId()));
    }
}
