package lans.hotels.datasource;

import lans.hotels.datasource.identity_maps.AbstractIdentityMapRegistry;
import lans.hotels.datasource.identity_maps.IIdentityMap;
import lans.hotels.datasource.mappers.IMapperRegistry;
import lans.hotels.datasource.unit_of_work.IUnitOfWork;
import lans.hotels.datasource.mappers.IMapper;
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

    protected DataSourceFacade(IUnitOfWork uow) {
        this.uow = uow;
    }

    protected void initMappers(IMapperRegistry mappers) {
        this.mappers = mappers;
    }

    protected void initIdentityMaps(AbstractIdentityMapRegistry<Integer> identityMaps) {
        this.identityMaps = identityMaps;
    }

    @Override
    public AbstractDomainObject find(Class<? extends AbstractDomainObject> aClass, Integer id) {
        AbstractDomainObject domainObject = checkCache(aClass, id);
        if (domainObject == null) {
            domainObject = queryDb(aClass, id);
            // TODO: identityMaps.get(className).put(id, domainObject);
        }
        return domainObject;
    }

    private AbstractDomainObject checkCache(Class<? extends AbstractDomainObject> aClass, Integer id) {
        IIdentityMap identityMap = identityMaps.get(aClass);
        if (identityMap != null) {
            return identityMap.getById(id);
        }
        return null;
    }

    private AbstractDomainObject queryDb(Class<? extends AbstractDomainObject> aClass, Integer id) {
        IMapper<Integer, AbstractDomainObject> identityMap = mappers.getMapper(aClass);
        AbstractDomainObject domainObject = identityMap.getById(id);
        return domainObject;
    }

}
