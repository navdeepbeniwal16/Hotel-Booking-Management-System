package lans.hotels.datasource.facade;

import lans.hotels.domain.AbstractDomainObject;

public interface IDataMapper<IdType, DomainObject extends AbstractDomainObject<IdType>> {
    DomainObject getById(IdType id);
    DomainObject create(DomainObject domainObject);
    DomainObject update(DomainObject domainObject);
    boolean delete(IdType id);
}
