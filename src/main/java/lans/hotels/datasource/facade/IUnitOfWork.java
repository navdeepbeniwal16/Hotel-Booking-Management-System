package lans.hotels.datasource.facade;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.domain.AbstractDomainObject;

public interface IUnitOfWork<IdType> {
    void registerNew(AbstractDomainObject obj) throws UoWException;
    void registerDirty(AbstractDomainObject obj) throws UoWException;
    void registerRemoved(AbstractDomainObject obj) throws UoWException;
    void registerClean(AbstractDomainObject obj) throws UoWException;
    void commit();
}
