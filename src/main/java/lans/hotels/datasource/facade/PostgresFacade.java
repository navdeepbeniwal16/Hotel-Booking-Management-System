package lans.hotels.datasource.facade;

import lans.hotels.datasource.exceptions.DataSourceLayerException;
import lans.hotels.datasource.identity_maps.HotelMap;
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

    @Override
    public <T extends AbstractDomainObject> T find(Class<T> aClass, Integer id) {
        IIdentityMap identityMap = identityMaps.get(aClass);
        IDataMapper mapper = mappers.getMapper(aClass);


        if (identityMap == null || mapper == null) return null;
        T domainObject = (T) identityMap.getById(id);

        if (domainObject == null) {
            domainObject = (T) mapper.getById(id);
        }

        return domainObject;
    }

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
