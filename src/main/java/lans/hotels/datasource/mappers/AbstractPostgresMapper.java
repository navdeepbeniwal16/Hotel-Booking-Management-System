package lans.hotels.datasource.mappers;


import lans.hotels.domain.IDomainObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import java.sql.Connection;
public abstract class AbstractPostgresMapper<DomainObject extends IDomainObject> implements IDataMapper<DomainObject> {
    protected Connection connection;
    protected Map<Integer, DomainObject> loadedMap = new HashMap();
    abstract protected String findStatement();

    protected AbstractPostgresMapper(Connection connection) {
        this.connection = connection;
    }

    protected DomainObject abstractGetById(int id) throws SQLException {
        DomainObject result = loadedMap.get(id);
        if (result == null) {
            result = getFromDb(id);
        }
        return result;
    }

    private DomainObject getFromDb(int id) throws SQLException {
        try (PreparedStatement findStatement = connection.prepareStatement(findStatement())){
            findStatement.setInt(1, id);
            ResultSet resultSet = findStatement.executeQuery();
            return load(resultSet);
        }
    }

    protected DomainObject load(ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) return null;

        int id = resultSet.getInt("id");
        DomainObject result = loadedMap.get(id);
        if (result == null) {
            result = doLoad(id, resultSet);
            loadedMap.put(id, result);
        }
        return result;
    }

    abstract protected DomainObject doLoad(int id, ResultSet resultSet) throws SQLException;
    abstract public DomainObject getById(int id);
}
