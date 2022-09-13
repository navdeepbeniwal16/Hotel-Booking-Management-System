package lans.hotels.datasource.identity_maps;

import lans.hotels.domain.AbstractDomainObject;

public interface IIdentityMap<IdType, ObjectType extends AbstractDomainObject> {
    void add(ObjectType obj);
    ObjectType getById(IdType id);
}
