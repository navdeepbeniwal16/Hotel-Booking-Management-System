package lans.hotels.datasource;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.concurrent.locks.ReentrantLock;

public class UnitOfWork {
    private static final String attributeName = "UnitOfWork";
    private volatile static HttpSession current;
    private static final ReentrantLock reentrantLock = new ReentrantLock();

    public UnitOfWork() {
    }

    synchronized public static UnitOfWork getCurrent() {
        return (UnitOfWork) UnitOfWork.current.getAttribute(attributeName);
    }

    private static void setCurrent(UnitOfWork uow) {
        reentrantLock.lock();
        UnitOfWork.current.setAttribute(attributeName, uow);
        reentrantLock.unlock();
    }

    private static void newCurrent() {
        setCurrent(new UnitOfWork());
    }

    public static void handleSession(HttpSession session) {
        reentrantLock.lock();
        current = session;
        if (session.getAttribute(attributeName) == null) {
            UnitOfWork.newCurrent();
        }
        reentrantLock.unlock();
    }
}
