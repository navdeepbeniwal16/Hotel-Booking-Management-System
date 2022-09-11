package lans.hotels.datasource;

import lans.hotels.domain.IReferenceObject;

public interface IIdentityMap<UIDType, ObjectType extends IReferenceObject> {
    void add(ObjectType obj);
    ObjectType get(UIDType id);
}
