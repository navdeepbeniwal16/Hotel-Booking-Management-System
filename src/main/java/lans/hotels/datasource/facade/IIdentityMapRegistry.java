package lans.hotels.datasource.facade;

import lans.hotels.domain.AbstractDomainObject;

import java.util.ArrayList;

public interface IIdentityMapRegistry<IdType> {
    IIdentityMap<IdType, ? extends AbstractDomainObject> get(Class<? extends AbstractDomainObject> aClass);
    ArrayList<IIdentityMap<IdType, ? extends AbstractDomainObject>> getAll();

}
