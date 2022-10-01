package lans.hotels.datasource.facade;

import lans.hotels.datasource.search_criteria.AbstractSearchCriteria;
import lans.hotels.domain.AbstractDomainObject;

import java.util.ArrayList;

public interface IDataMapper<DomainObject extends AbstractDomainObject> {
    <DomainObject extends AbstractDomainObject> DomainObject getById(Integer id);
    <DomainObject extends AbstractDomainObject> Boolean insert(DomainObject domainObject) throws Exception;
    <DomainObject extends AbstractDomainObject> DomainObject update(AbstractDomainObject domainObject) throws Exception;
    boolean delete(Integer id);

    ArrayList<DomainObject> findAll() throws Exception;
    <DomainObj extends AbstractDomainObject> ArrayList<DomainObj> findBySearchCriteria(AbstractSearchCriteria criteria) throws Exception;
}
