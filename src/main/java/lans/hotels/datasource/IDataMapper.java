package lans.hotels.datasource;

import lans.hotels.domain.IDomainObject;

public interface IDataMapper<DomainObject extends IDomainObject> {
    DomainObject getById(int id);
    DomainObject create(DomainObject domainObject);
//   DomainObject update(int id, DomainObject domainObject);
//    boolean delete(int id);
}
