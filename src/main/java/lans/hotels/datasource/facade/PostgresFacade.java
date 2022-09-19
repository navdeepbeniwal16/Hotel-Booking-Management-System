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
            PostgresFacade facade = new PostgresFacade(connection);

            IntegerIdentityMapRegistry identityMaps = ServletUoW.getActiveIdentityMaps(session);
            if (identityMaps == null) identityMaps = IntegerIdentityMapRegistry.newInstance(facade);

            ServletUoW.handleSession(session, identityMaps);

            PostgresMapperRegistry mappers = PostgresMapperRegistry.newInstance(connection, facade);

            facade.initIdentityMaps(identityMaps);
            facade.initMappers(mappers);
            facade.initUoW(ServletUoW.getCurrent());

            return facade;
        } catch (Exception e) {
            throw new DataSourceLayerException(e.getMessage());
        }
    }

    private PostgresFacade(Connection connection) {
        super(connection);
    }
}
