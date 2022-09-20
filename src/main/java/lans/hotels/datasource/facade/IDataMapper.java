package lans.hotels.datasource.facade;

import lans.hotels.datasource.search_criteria.AbstractSearchCriteria;
import lans.hotels.domain.AbstractDomainObject;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IDataMapper<DomainObject extends AbstractDomainObject> {
    <DomainObj extends AbstractDomainObject> DomainObj getById(Integer id) throws SQLException;
    <DomainObj extends AbstractDomainObject> void create(AbstractDomainObject domainObject) throws SQLException;
    <DomainObj extends AbstractDomainObject> DomainObj update(AbstractDomainObject domainObject);
    boolean delete(Integer id);

    ArrayList<DomainObject> findAll() throws Exception;
    <DomainObj extends AbstractDomainObject> ArrayList<DomainObj> findBySearchCriteria(AbstractSearchCriteria criteria) throws Exception;
}
