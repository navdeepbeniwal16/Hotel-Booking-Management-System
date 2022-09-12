package lans.hotels.datasource;

import lans.hotels.datasource.exceptions.UnitOfWorkException;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class UnitOfWork implements AbstractUoW {
    private static final String attributeName = "UnitOfWork";
    private static HashMap<Thread, UnitOfWork> activeUnitsOfWork = new HashMap<>();
    private static final ReentrantLock reentrantLock = new ReentrantLock();

    public UnitOfWork() {
    }

    synchronized public static UnitOfWork getCurrent() throws UnitOfWorkException {
        UnitOfWork current = activeUnitsOfWork.get(Thread.currentThread());
        if (current == null) {
            throw new UnitOfWorkException("Unit of Work was not initialised for this thread. UnitOfWork.handleSession() must be called in order to associate a UoW instance with a thread");
        }
        return current;
    }

    public static void handleSession(HttpSession session) {
        reentrantLock.lock();
        UnitOfWork uow = (UnitOfWork) session.getAttribute(attributeName);
        if (uow == null) {
            uow = UnitOfWork.newActiveUoW();
        }
        activeUnitsOfWork.put(Thread.currentThread(), uow);
        reentrantLock.unlock();
    }

    private static UnitOfWork newActiveUoW() {
        return new UnitOfWork();
    }

    public static void removeCurrent() {
        // TODO: exception handling?
        activeUnitsOfWork.remove(Thread.currentThread());
    }
}
