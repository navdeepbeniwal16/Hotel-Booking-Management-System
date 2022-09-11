package lans.hotels.datasource;

import lans.hotels.domain.AbstractDomainObject;

public interface IIdentityMap<UIDType, ObjectType extends AbstractDomainObject> {
    void add(ObjectType obj);
    ObjectType get(UIDType id);
}
