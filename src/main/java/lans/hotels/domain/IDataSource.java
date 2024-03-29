package lans.hotels.domain;

import lans.hotels.datasource.exceptions.DataSourceLayerException;
import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.search_criteria.AbstractSearchCriteria;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface IDataSource {
    void load(AbstractDomainObject domainObject);
    <DomainObject extends AbstractDomainObject> ArrayList<DomainObject> findAll(Class<DomainObject> aClass) throws Exception;
    <DomainObject extends AbstractDomainObject> DomainObject find(Class<DomainObject> aClass, Integer id) throws SQLException;
    <DomainObject extends AbstractDomainObject> ArrayList<DomainObject> findBySearchCriteria(Class<DomainObject> aClass, AbstractSearchCriteria criteria) throws Exception;
    void registerNew(AbstractDomainObject domainObject) throws UoWException;
    void registerDirty(AbstractDomainObject domainObject) throws UoWException;
    void registerRemoved(AbstractDomainObject domainObject) throws UoWException;
    void registerClean(AbstractDomainObject domainObject) throws UoWException;

    // TODO: #refactor - split into two separate interfaces. One of Data Model and one for Controllers.
    // Why? Current Data Model is receiving an interface it should not be able to use (commit).
    void commit() throws DataSourceLayerException;

    <DomainObject extends AbstractDomainObject> Integer insert(Class<DomainObject> aClass, DomainObject object)throws Exception;
    <DomainObject extends AbstractDomainObject> Boolean delete(Class<DomainObject> aClass,Integer id)throws Exception;
    boolean isOpen();
}
