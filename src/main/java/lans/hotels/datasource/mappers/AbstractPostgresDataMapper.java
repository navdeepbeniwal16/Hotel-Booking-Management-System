package lans.hotels.datasource.mappers;


import lans.hotels.datasource.facade.IDataMapper;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import java.sql.Connection;
public abstract class AbstractPostgresDataMapper<DomainObject extends AbstractDomainObject<Integer>>
        implements IDataMapper<Integer, DomainObject> {
    protected IDataSource dataSource;
    protected Connection connection;
    protected String table;
    protected Map<Integer, DomainObject> loadedMap = new HashMap();
    abstract protected String findStatement();
    abstract protected String insertStatement();
    protected abstract DomainObject doLoad(Integer id, ResultSet resultSet) throws SQLException;
    public abstract DomainObject doCreate(DomainObject domainObject);

    protected AbstractPostgresDataMapper(Connection connection, String table, IDataSource dataSource) {
        this.connection = connection;
        this.table = table;
        this.dataSource = dataSource;
    }

    protected DomainObject abstractGetById(int id) throws SQLException {
        return getFromDb(id);
    }

    public DomainObject getById(Integer id) {
        try {
            return abstractGetById(id);
        } catch (SQLException e) {
            System.err.println("AbstractPostgresDataMapper.getById(): " + e.getMessage());
            // TODO: do not return null!
            return null;
        }
    }

    private DomainObject getFromDb(Integer id) throws SQLException {
        try (PreparedStatement findStatement = connection.prepareStatement(findStatement())){
            findStatement.setInt(1, id);
            ResultSet resultSet = findStatement.executeQuery();
            return load(resultSet);
        }
    }
    protected DomainObject load(ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) return null;

        // TODO: abstract out type to generic?

        Integer id = resultSet.getInt("id");
        DomainObject result = loadedMap.get(id);
        if (result == null) {
            result = doLoad(id, resultSet);
            loadedMap.put(id, result);
        }
        return result;
    }

    public DomainObject create(DomainObject domainObject) {
        DomainObject newDomainObject = doCreate(domainObject);
        if (newDomainObject != null) loadedMap.put(newDomainObject.getId(), newDomainObject);
        return newDomainObject;
    }
}
