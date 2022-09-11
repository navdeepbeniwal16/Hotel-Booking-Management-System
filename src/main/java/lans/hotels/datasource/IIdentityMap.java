package lans.hotels.datasource;

import lans.hotels.domain.IDomainObject;

public interface IIdentityMap<UIDType, ObjectType extends IDomainObject> {
    void add(ObjectType obj);
    ObjectType get(UIDType id);
}
