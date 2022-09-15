package lans.hotels.datasource.facade;

import lans.hotels.datasource.identity_maps.AbstractIdentityMapRegistry;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;

public abstract class DataSourceFacade implements IDataSource<Integer> {
    IUnitOfWork uow;
    IMapperRegistry<Integer> mappers;
    AbstractIdentityMapRegistry<Integer> identityMaps;


    protected DataSourceFacade(IUnitOfWork uow, IMapperRegistry mappers, AbstractIdentityMapRegistry<Integer> identityMaps) {
        this.uow = uow;
        this.mappers = mappers;
        this.identityMaps = identityMaps;
    }

    protected DataSourceFacade() {
    }

    protected void initUoW(IUnitOfWork uow) {
        this.uow = uow;
    }

    protected void initMappers(IMapperRegistry mappers) {
        this.mappers = mappers;
    }

    protected void initIdentityMaps(AbstractIdentityMapRegistry<Integer> identityMaps) {
        this.identityMaps = identityMaps;
    }

    public <T extends AbstractDomainObject> T find(Class<T> aClass, Integer id) {
        IIdentityMap classCache = identityMaps.get(aClass);
        IDataMapper<Integer, AbstractDomainObject<Integer>> mapper = mappers.getMapper(aClass);
        T domainObject = (T) classCache.getById(id);
        if (domainObject == null) {
            domainObject = (T) mapper.getById(id);
            if (domainObject != null) {
                classCache.add(domainObject);
            }
        }
        return domainObject;
    }

    public void load(AbstractDomainObject domainObject) {

    }

    public void registerNew(AbstractDomainObject domainObject) {
        uow.registerNew(domainObject);
    }

    public void registerDirty(AbstractDomainObject domainObject) {
        uow.registerDirty(domainObject);
    }

    public void registerRemoved(AbstractDomainObject domainObject) {
        uow.registerRemoved(domainObject);
    }

    public void registerClean(AbstractDomainObject domainObject) {
        uow.registerClean(domainObject);
    }
}
