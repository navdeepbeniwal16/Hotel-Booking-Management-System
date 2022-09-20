package lans.hotels.datasource.mappers;

import lans.hotels.datasource.search_criteria.AbstractSearchCriteria;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.room.RoomSpecification;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RoomSpecificationMapper extends AbstractPostgresDataMapper<RoomSpecification> {

    public RoomSpecificationMapper(Connection connection, IDataSource dataSource) {
        super(connection, "room_spec", dataSource);
    }

    @Override
    public <DomainObject extends AbstractDomainObject> Boolean create(DomainObject domainObject) {
        return null;
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
    public ArrayList<RoomSpecification> findAll() throws SQLException {
        return null;
    }

    @Override
    public ArrayList<RoomSpecification> insert() throws Exception {
        return null;
    }

//    public RoomSpecMapper(Connection connection, IDataSource dataSource) {
//        super(connection, "room_spec", dataSource);
//    }

}
