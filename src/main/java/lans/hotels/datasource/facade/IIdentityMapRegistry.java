package lans.hotels.datasource.facade;

import lans.hotels.domain.AbstractDomainObject;

public interface IIdentityMapRegistry<IdType> {
    IIdentityMap<IdType, AbstractDomainObject> get(Class<?> aClass);
}
