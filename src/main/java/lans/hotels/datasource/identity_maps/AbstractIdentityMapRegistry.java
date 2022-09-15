package lans.hotels.datasource.identity_maps;

import lans.hotels.datasource.facade.IIdentityMap;
import lans.hotels.datasource.facade.IIdentityMapRegistry;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;

import java.util.Map;

public abstract class AbstractIdentityMapRegistry<IdType> implements IIdentityMapRegistry<IdType>{
    private Map<String, IIdentityMap<IdType, AbstractDomainObject>> identityMaps;
    private IDataSource dataSource;
    protected AbstractIdentityMapRegistry(Map<String, IIdentityMap<IdType, AbstractDomainObject>> identityMaps, IDataSource dataSource) {
        this.identityMaps = identityMaps;
        this.dataSource = dataSource;
    }

    public IIdentityMap<IdType, AbstractDomainObject> get(Class<?> aClass) {
        return identityMaps.get(aClass.getName());
    }
}
