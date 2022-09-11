package lans.hotels.datasource;

import lans.hotels.domain.IReferenceObject;

public interface IDataMapper<DomainObject extends IReferenceObject> {
    DomainObject getById(int id);
    DomainObject create(DomainObject domainObject);
//   DomainObject update(int id, DomainObject domainObject);
//    boolean delete(int id);
}
