package lans.hotels.datasource.identity_maps;

import lans.hotels.datasource.facade.IIdentityMap;
import lans.hotels.datasource.facade.IIdentityMapRegistry;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;

import java.util.HashMap;

public abstract class AbstractIdentityMapRegistry implements IIdentityMapRegistry {
    protected HashMap<String, IIdentityMap<? extends AbstractDomainObject>> identityMaps;
    protected IDataSource dataSource;
    protected AbstractIdentityMapRegistry(HashMap<String, IIdentityMap<? extends AbstractDomainObject>> identityMaps, IDataSource dataSource) {
        this.identityMaps = identityMaps;
        this.dataSource = dataSource;
    }

    public  IIdentityMap<? extends AbstractDomainObject> get(Class<? extends AbstractDomainObject> aClass) {
        return identityMaps.get(aClass.getName()); // TODO: #bug unchecked cast?
    }

    public void add(IIdentityMap<? extends AbstractDomainObject> identityMap, Class<?> aClass) {
        identityMaps.put(aClass.getName(), identityMap);
    }
}
