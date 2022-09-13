package lans.hotels.datasource.unit_of_work;

import lans.hotels.datasource.exceptions.UnitOfWorkException;
import lans.hotels.datasource.identity_maps.AbstractIdentityMapRegistry;
import lans.hotels.datasource.identity_maps.IntegerIdentityMapRegistry;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class ServletUoW implements IUnitOfWork{
    public AbstractIdentityMapRegistry identityMaps;
    private static final String attributeName = "UnitOfWork";
    private static HashMap<Thread, ServletUoW> activeUnitsOfWork = new HashMap<>();
    private static final ReentrantLock reentrantLock = new ReentrantLock();

    public ServletUoW(AbstractIdentityMapRegistry identityMaps) {
        this.identityMaps = identityMaps;
    }

    synchronized public static ServletUoW getCurrent() throws UnitOfWorkException {
        ServletUoW current = activeUnitsOfWork.get(Thread.currentThread());
        if (current == null) {
            throw new UnitOfWorkException("Unit of Work was not initialised for this thread. UnitOfWork.handleSession() must be called in order to associate a UoW instance with a thread");
        }
        return current;
    }

    public static void handleSession(HttpSession session, IntegerIdentityMapRegistry identityMaps) {
        reentrantLock.lock();
        ServletUoW uow = (ServletUoW) session.getAttribute(attributeName);
        if (uow == null) {
            uow = ServletUoW.newActiveUoW(identityMaps);
        }
        activeUnitsOfWork.put(Thread.currentThread(), uow);
        reentrantLock.unlock();
    }

    private static ServletUoW newActiveUoW(IntegerIdentityMapRegistry identityMaps) {
        return new ServletUoW(identityMaps);
    }

    public static void removeCurrent() {
        // TODO: exception handling?
        activeUnitsOfWork.remove(Thread.currentThread());
    }
}
