package lans.hotels.datasource.mappers;

import lans.hotels.datasource.search_criteria.AbstractSearchCriteria;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.room.RoomSpecification;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RoomSpecMapper extends AbstractPostgresDataMapper<RoomSpecification> {
    protected RoomSpecMapper(Connection connection, String table, IDataSource dataSource) {
        super(connection, table, dataSource);
    }

    @Override
    public <DomainObj extends AbstractDomainObject> DomainObj update(AbstractDomainObject domainObject) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public <DomainObj extends AbstractDomainObject> ArrayList<DomainObj> findBySearchCriteria(AbstractSearchCriteria criteria) throws Exception {
        return null;
    }

    @Override
    protected String findStatement() {
        return null;
    }

    @Override
    protected String insertStatement() {
        return null;
    }

    @Override
    protected RoomSpecification doLoad(Integer id, ResultSet resultSet) throws SQLException {
        return null;
    }

    @Override
    public RoomSpecification doCreate(RoomSpecification domainObject) {
        return null;
    }

    @Override
    public ArrayList<RoomSpecification> findAll() throws SQLException {
        return null;
    }

//    public RoomSpecMapper(Connection connection, IDataSource dataSource) {
//        super(connection, "room_spec", dataSource);
//    }

}
