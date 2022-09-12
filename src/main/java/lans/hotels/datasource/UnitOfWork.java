package lans.hotels.datasource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.concurrent.locks.ReentrantLock;

public class UnitOfWork implements AbstractUoW {
    private static final String attributeName = "UnitOfWork";
    private static HttpServletRequest current;
    private static final ReentrantLock reentrantLock = new ReentrantLock();

    public UnitOfWork() {
    }

    synchronized public static UnitOfWork getCurrent() {
        return (UnitOfWork) UnitOfWork.current.getSession().getAttribute(attributeName);
    }

    private static void setCurrent(UnitOfWork uow) {
        UnitOfWork.current.getSession().setAttribute(attributeName, uow);
    }

    private static void newCurrent() {
        setCurrent(new UnitOfWork());
    }

    public static void handleRequest(HttpServletRequest request) {
        reentrantLock.lock();
        current = request;
        if (current.getSession().getAttribute(attributeName) == null) {
            UnitOfWork.newCurrent();
        }
        reentrantLock.unlock();
    }
}
