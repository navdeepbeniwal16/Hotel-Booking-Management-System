package lans.hotels.datasource.facade;

import lans.hotels.datasource.exceptions.IdentityMapException;
import lans.hotels.domain.AbstractDomainObject;

public interface IIdentityMap<IdType, ObjectType extends AbstractDomainObject> {
    void add(ObjectType obj) throws IdentityMapException;
    ObjectType getById(IdType id);
    ObjectType remove(IdType id);

}
