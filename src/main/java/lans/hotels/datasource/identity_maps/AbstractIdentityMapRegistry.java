package lans.hotels.datasource.identity_maps;

import lans.hotels.datasource.facade.IIdentityMap;
import lans.hotels.datasource.facade.IIdentityMapRegistry;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;

import java.util.Map;

public abstract class AbstractIdentityMapRegistry<IdType> implements IIdentityMapRegistry<IdType> {
    protected Map<String, IIdentityMap<IdType, ? extends AbstractDomainObject>> identityMaps;
    protected IDataSource dataSource;
    protected AbstractIdentityMapRegistry(Map<String, IIdentityMap<IdType, ? extends AbstractDomainObject>> identityMaps, IDataSource dataSource) {
        this.identityMaps = identityMaps;
        this.dataSource = dataSource;
    }

    public IIdentityMap<IdType, ? extends AbstractDomainObject> get(Class<? extends AbstractDomainObject> aClass) {
        return identityMaps.get(aClass.getName());
    }
}
