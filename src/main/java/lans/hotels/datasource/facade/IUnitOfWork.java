package lans.hotels.datasource.facade;

import lans.hotels.domain.AbstractDomainObject;

public interface IUnitOfWork<IdType> {
    void registerNew(AbstractDomainObject obj);
    void registerDirty(AbstractDomainObject obj);
    void registerRemoved(AbstractDomainObject obj);
    void registerClean(AbstractDomainObject obj);
    void commit();
}
