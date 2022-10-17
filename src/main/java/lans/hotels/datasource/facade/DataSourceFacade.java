package lans.hotels.datasource.facade;

import lans.hotels.datasource.exceptions.DataSourceLayerException;
import lans.hotels.datasource.exceptions.IdentityMapException;
import lans.hotels.datasource.exceptions.MapperNotFoundException;
import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.identity_maps.AbstractIdentityMapRegistry;
import lans.hotels.datasource.mappers.AbstractPostgresDataMapper;
import lans.hotels.datasource.search_criteria.AbstractSearchCriteria;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class DataSourceFacade implements IDataSource {
    Connection connection;
    IUnitOfWork uow;
    IMapperRegistry dataMapperRegistry;
    AbstractIdentityMapRegistry identityMapRegistry;

    protected DataSourceFacade(Connection connection) {
        this.connection = connection;
    }

    protected void initUoW(IUnitOfWork uow) {
        this.uow = uow;
    }

    protected void initMappers(IMapperRegistry mappers) {
        this.dataMapperRegistry = mappers;
    }

    protected void initIdentityMaps(AbstractIdentityMapRegistry identityMaps) {
        this.identityMapRegistry = identityMaps;
    }

    public <DomainObject extends AbstractDomainObject> DomainObject find(Class<DomainObject> aClass, Integer id) throws SQLException {
        // TODO: #review - does this need to be refactored? Now that UoW is clearing Identity Maps every request?
        IIdentityMap classCache = identityMapRegistry.get(aClass);
        IDataMapper<AbstractDomainObject> mapper;
        try {
            mapper = dataMapperRegistry.getMapper((Class<AbstractDomainObject>) aClass); // TODO: #bug - possible unchecked caste
        } catch (MapperNotFoundException e) {
            System.err.println(e);
            return null;
        }
        DomainObject domainObject = (DomainObject) classCache.getById(id); // TODO: #bug - possible unchecked caste
        if (domainObject == null) {
            domainObject = (DomainObject) mapper.getById(id); // TODO: #bug - possible unchecked caste
            if (domainObject != null) {
                // TODO: handle this exception more gracefully
                try { classCache.add(domainObject); } catch (IdentityMapException e) {}
            }
        }
        return domainObject;
    }

    public <DomainObject extends AbstractDomainObject> ArrayList<DomainObject> findAll(Class<DomainObject> aClass) throws Exception {
        AbstractPostgresDataMapper<DomainObject> mapper;
        try {
            mapper = dataMapperRegistry.getMapper(aClass);
            if (mapper==null) {
                System.err.println("Null mapper for class: " + aClass.getName());
            } else {
//                System.out.println("DataSourceFacade.findAll(): getting objects for class" + aClass.getName());
                return mapper.findAll();
            }
        } catch (Exception e) {
            System.err.println("ERROR DataSourceFacade.findAll(): " + e);
            throw e;
        }
        return null;
    }

    public <DomainObject extends AbstractDomainObject> Integer insert(Class<DomainObject> aClass,DomainObject object) throws Exception {
        AbstractPostgresDataMapper<? extends  AbstractDomainObject> mapper = dataMapperRegistry.getMapper(aClass); //TODO: #bug unchecked type caste
        return mapper.insert(object);
    }

    public <DomainObject extends AbstractDomainObject> Boolean delete(Class<DomainObject> aClass,Integer id) throws Exception {
        AbstractPostgresDataMapper<? extends  AbstractDomainObject> mapper = dataMapperRegistry.getMapper(aClass); //TODO: #bug unchecked type caste
        return (Boolean) mapper.delete(id);
    }

    public <DomainObject extends AbstractDomainObject> ArrayList<DomainObject> findBySearchCriteria(Class<DomainObject> aClass, AbstractSearchCriteria criteria) throws Exception {
        AbstractPostgresDataMapper<? extends  AbstractDomainObject> mapper = dataMapperRegistry.getMapper(aClass); //TODO: #bug unchecked type caste
        return mapper.findBySearchCriteria(criteria);
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
            if (isOpen()) {
                uow.commit(connection, dataMapperRegistry);
            }
        } catch (Exception e) {
            throw new DataSourceLayerException(e.getMessage());
        }
    }

    public boolean isOpen() {
        try {
            return this.connection != null && !this.connection.isClosed();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
