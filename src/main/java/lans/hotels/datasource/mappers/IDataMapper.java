package lans.hotels.datasource.mappers;

import lans.hotels.domain.IDomainObject;

public interface IDataMapper<T extends IDomainObject> {
    T getById(int id);
//    T create(T domainObject);
//    T update(int id, T domainObject);
//    boolean delete(int id);
}
