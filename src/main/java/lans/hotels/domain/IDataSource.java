package lans.hotels.domain;

import lans.hotels.datasource.exceptions.DataSourceLayerException;
import lans.hotels.datasource.exceptions.UoWException;

public interface IDataSource<IdType> {
    void load(AbstractDomainObject domainObject);
    <T extends AbstractDomainObject> T find(Class<T> aClass, IdType id);
    void registerNew(AbstractDomainObject domainObject) throws UoWException;
    void registerDirty(AbstractDomainObject domainObject) throws UoWException;
    void registerRemoved(AbstractDomainObject domainObject) throws UoWException;
    void registerClean(AbstractDomainObject domainObject) throws UoWException;

    // TODO: #refactor - split into two separate interfaces. One of Data Model and one for Controllers.
    // Why? Current Data Model is receiving an interface it should not be able to use (commit).
    void commit() throws DataSourceLayerException;
}
