package lans.hotels.datasource;

import lans.hotels.domain.AbstractDomainObject;

public abstract class AbstractUoW {
    public abstract void registerNew(AbstractDomainObject obj);
    public abstract void registerDirty(AbstractDomainObject obj);
    public abstract void registerRemoved(AbstractDomainObject obj);
    public abstract void registerClean(AbstractDomainObject obj);
}
