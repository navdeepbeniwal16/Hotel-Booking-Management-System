package lans.hotels.datasource.facade;

import lans.hotels.datasource.search_criteria.AbstractSearchCriteria;
import lans.hotels.domain.AbstractDomainObject;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IDataMapper<DomainObject extends AbstractDomainObject> {
    <DomainObject extends AbstractDomainObject> DomainObject getById(Integer id);
    <DomainObject extends AbstractDomainObject> Boolean create(DomainObject domainObject) throws Exception;
    <DomainObject extends AbstractDomainObject> DomainObject update(AbstractDomainObject domainObject);
    boolean delete(Integer id);

    ArrayList<DomainObject> findAll() throws Exception;
    ArrayList<DomainObject> insert() throws Exception;
    <DomainObj extends AbstractDomainObject> ArrayList<DomainObj> findBySearchCriteria(AbstractSearchCriteria criteria) throws Exception;
}
