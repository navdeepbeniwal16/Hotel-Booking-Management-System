package lans.hotels.datasource.facade;

import lans.hotels.domain.AbstractDomainObject;

import java.util.ArrayList;

public interface IIdentityMapRegistry {
    IIdentityMap<? extends AbstractDomainObject> get(Class<? extends AbstractDomainObject> aClass);
    ArrayList<IIdentityMap<? extends AbstractDomainObject>> getAll();
}
