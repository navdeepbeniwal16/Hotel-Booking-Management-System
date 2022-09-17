package lans.hotels.datasource.facade;

import lans.hotels.datasource.exceptions.DataSourceLayerException;
import lans.hotels.datasource.exceptions.IdentityMapException;
import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.identity_maps.AbstractIdentityMapRegistry;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;

import java.sql.Connection;

public abstract class DataSourceFacade implements IDataSource<Integer> {
    Connection connection;
    IUnitOfWork uow;
    IMapperRegistry<Integer> dataMapperRegistry;
    AbstractIdentityMapRegistry<Integer> identityMapRegistry;

    protected DataSourceFacade(Connection connection) {
        this.connection = connection;
    }

    protected void initUoW(IUnitOfWork uow) {
        this.uow = uow;
    }

    protected void initMappers(IMapperRegistry mappers) {
        this.dataMapperRegistry = mappers;
    }

    protected void initIdentityMaps(AbstractIdentityMapRegistry<Integer> identityMaps) {
        this.identityMapRegistry = identityMaps;
    }

    public <T extends AbstractDomainObject> T find(Class<T> aClass, Integer id) {
        IIdentityMap classCache = identityMapRegistry.get(aClass);
        IDataMapper<Integer, AbstractDomainObject<Integer>> mapper = dataMapperRegistry.getMapper(aClass);
        T domainObject = (T) classCache.getById(id);
        if (domainObject == null) {
            domainObject = (T) mapper.getById(id);
            if (domainObject != null) {
                // TODO: handle this exception more gracefully
                try { classCache.add(domainObject); } catch (IdentityMapException e) {}
            }
        }
        return domainObject;
    }

    public void load(AbstractDomainObject domainObject) {

    }

    public void registerNew(AbstractDomainObject domainObject) throws UoWException {
        uow.registerNew(domainObject);
    }

    public void registerDirty(AbstractDomainObject domainObject) throws UoWException {
        uow.registerDirty(domainObject);
    }

    public void registerRemoved(AbstractDomainObject domainObject) throws UoWException {
        uow.registerRemoved(domainObject);
    }

    public void registerClean(AbstractDomainObject domainObject) throws UoWException {
        uow.registerClean(domainObject);
    }

    public void commit() throws DataSourceLayerException {
        try {
            uow.commit(dataMapperRegistry);
            connection.commit();
        } catch (Exception e) {
            throw new DataSourceLayerException(e.getMessage());
        }
    }
}
