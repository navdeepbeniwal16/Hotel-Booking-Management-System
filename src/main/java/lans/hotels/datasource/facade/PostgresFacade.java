package lans.hotels.datasource.facade;

import lans.hotels.datasource.exceptions.DataSourceLayerException;
import lans.hotels.datasource.identity_maps.IntegerIdentityMapRegistry;
import lans.hotels.datasource.mappers.PostgresMapperRegistry;
import lans.hotels.datasource.unit_of_work.ServletUoW;
import lans.hotels.domain.AbstractDomainObject;

import javax.servlet.http.HttpSession;
import java.sql.Connection;

public class PostgresFacade extends DataSourceFacade {
    public static PostgresFacade newInstance(HttpSession session,
                                             Connection connection) throws DataSourceLayerException {
        try {

            PostgresFacade facade = new PostgresFacade();


            IntegerIdentityMapRegistry identityMaps = IntegerIdentityMapRegistry.newInstance(facade);
            PostgresMapperRegistry mappers = PostgresMapperRegistry.newInstance(connection, facade);


            facade.initIdentityMaps(identityMaps);
            facade.initMappers(mappers);
            // TODO: unfuck this section
            ServletUoW.handleSession(session, identityMaps);
            facade.initUoW(ServletUoW.getCurrent());

            return facade;
        } catch (Exception e) {
            throw new DataSourceLayerException(e.getMessage());
        }
    }

    private PostgresFacade() {
        super();
    }

    @Override
    public void load(AbstractDomainObject domainObject) {

    }

//    @Override
//    public <T extends AbstractDomainObject> T find(Class<T> aClass, Integer id) {
//        IIdentityMap cache = identityMaps.get(aClass);
//        IDataMapper dataMapper = mappers.getMapper(aClass);
//
//
//        // TODO: remove later
////        if (cache == null || dataMapper == null) return null; // TODO: might be an issue...
//
//        // Check the cache first
//        T domainObject = (T) cache.getById(id);
//
//        // If it is not in the cache, then check the database.
//        if (domainObject == null) {
//            domainObject = (T) dataMapper.getById(id);
//        }
//
//        if (domainObject != null) {
//            cache.add(domainObject);
//            return domainObject;
//        }
//
//        return null;
//    }

    @Override
    public void registerNew(AbstractDomainObject domainObject) {

    }

    @Override
    public void registerDirty(AbstractDomainObject domainObject) {

    }

    @Override
    public void registerRemoved(AbstractDomainObject domainObject) {

    }

    @Override
    public void registerClean(AbstractDomainObject domainObject) {

    }
}
