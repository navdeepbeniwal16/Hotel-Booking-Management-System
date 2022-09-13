package lans.hotels.datasource;

import lans.hotels.datasource.exceptions.DataSourceLayerException;
import lans.hotels.datasource.identity_maps.IntegerIdentityMapRegistry;
import lans.hotels.datasource.mappers.IMapperRegistry;
import lans.hotels.datasource.mappers.PostgresMapperRegistry;
import lans.hotels.datasource.unit_of_work.IUnitOfWork;
import lans.hotels.datasource.unit_of_work.ServletUoW;
import lans.hotels.domain.AbstractDomainObject;

import javax.servlet.http.HttpSession;
import java.sql.Connection;

public class PostgresFacade extends DataSourceFacade {
    public static PostgresFacade newInstance(HttpSession session,
                                             Connection connection) throws DataSourceLayerException {
        try {
            IntegerIdentityMapRegistry identityMaps = IntegerIdentityMapRegistry.newInstance();
            ServletUoW.handleSession(session, identityMaps);
            PostgresFacade facade = new PostgresFacade(ServletUoW.getCurrent(), connection);
            facade.initIdentityMaps(identityMaps);
            return facade;
        } catch (Exception e) {
            throw new DataSourceLayerException(e.getMessage());
        }
    }

    private PostgresFacade(IUnitOfWork uow, Connection connection) {
        super(uow);
        IMapperRegistry mappers = PostgresMapperRegistry.newInstance(connection, this);
        initMappers(mappers);
    }

    @Override
    public void load(AbstractDomainObject domainObject) {

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
