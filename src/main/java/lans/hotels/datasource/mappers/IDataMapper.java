package lans.hotels.datasource.mappers;

import lans.hotels.domain.IDomainObject;

public interface IDataMapper<DomainObject extends IDomainObject> {
    DomainObject getById(int id);
    DomainObject create(DomainObject domainObject);
//    T update(int id, T domainObject);
//    boolean delete(int id);
}
