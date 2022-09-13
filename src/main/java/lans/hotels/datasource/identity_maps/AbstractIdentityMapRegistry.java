package lans.hotels.datasource.identity_maps;

import lans.hotels.datasource.facade.IIdentityMap;
import lans.hotels.datasource.facade.IIdentityMapRegistry;
import lans.hotels.domain.AbstractDomainObject;

import java.util.Map;

public abstract class AbstractIdentityMapRegistry<IdType> implements IIdentityMapRegistry<IdType>{
    private Map<String, IIdentityMap<IdType, AbstractDomainObject>> identityMaps;

    protected AbstractIdentityMapRegistry(Map<String, IIdentityMap<IdType, AbstractDomainObject>> identityMaps) {
        this.identityMaps = identityMaps;
    }

    public IIdentityMap<IdType, AbstractDomainObject> get(Class<?> aClass) {
        return identityMaps.get(aClass.getName());
    }
}
