package lans.hotels.datasource.facade;

import lans.hotels.domain.AbstractDomainObject;

public interface IIdentityMapRegistry<IdType> {
    IIdentityMap<IdType, ? extends AbstractDomainObject> get(Class<? extends AbstractDomainObject> aClass);
}
