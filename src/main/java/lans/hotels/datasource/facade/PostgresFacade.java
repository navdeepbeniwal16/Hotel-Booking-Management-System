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
            PostgresFacade facade = new PostgresFacade(ServletUoW.getCurrent(), connection);
            IntegerIdentityMapRegistry identityMaps = IntegerIdentityMapRegistry.newInstance(facade);
            PostgresMapperRegistry mappers = PostgresMapperRegistry.newInstance(connection, facade);
            ServletUoW.handleSession(session, identityMaps);
            facade.initIdentityMaps(identityMaps);
            facade.initMappers(mappers);

            return facade;
        } catch (Exception e) {
            throw new DataSourceLayerException(e.getMessage());
        }
    }

    private PostgresFacade(IUnitOfWork uow, Connection connection) {
        super(uow);
    }

    @Override
    public void load(AbstractDomainObject domainObject) {

    }

    @Override
    public <T extends AbstractDomainObject> T find(Class<T> aClass, Integer id) {
        return null;
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
