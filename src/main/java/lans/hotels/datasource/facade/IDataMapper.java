package lans.hotels.datasource.facade;

import lans.hotels.datasource.search_criteria.AbstractSearchCriteria;
import lans.hotels.domain.AbstractDomainObject;

import java.util.List;

public interface IDataMapper<IdType, DomainObject extends AbstractDomainObject<IdType>> {
    DomainObject getById(IdType id);
    DomainObject create(DomainObject domainObject);
    DomainObject update(DomainObject domainObject);
    boolean delete(IdType id);

    List<DomainObject> findAll() throws Exception;
    List<DomainObject> findBySearchCriteria(AbstractSearchCriteria criteria) throws Exception;
}
