package lans.hotels.datasource;

import lans.hotels.domain.AbstractDomainObject;

public interface IDataMapper<DomainObject extends AbstractDomainObject> {
    DomainObject getById(int id);
    DomainObject create(DomainObject domainObject);
//   DomainObject update(int id, DomainObject domainObject);
//    boolean delete(int id);
}
