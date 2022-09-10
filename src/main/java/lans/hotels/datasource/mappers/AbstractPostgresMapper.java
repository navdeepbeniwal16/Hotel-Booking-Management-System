package lans.hotels.datasource.mappers;


import lans.hotels.domain.IDomainObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import java.sql.Connection;
public abstract class AbstractPostgresMapper implements IDataMapper {
    protected Connection connection;
    protected Map<Integer, IDomainObject> loadedMap = new HashMap();
    abstract protected String findStatement();

    protected AbstractPostgresMapper(Connection connection) {
        this.connection = connection;
    }

    protected IDomainObject abstractGetById(int id) {
        IDomainObject result = loadedMap.get(id);
        if (result == null) {
            result = getFromDb(int id);
        }
        return result;
    }

    private IDomainObject getFromDb(int id) throws SQLException {
        try (PreparedStatement findStatement = connection.prepareStatement(findStatement())){
            findStatement.setInt(1, id);
            ResultSet resultSet = findStatement.executeQuery();
            resultSet.next();
            return load(resultSet);
        }
    }

    protected IDomainObject load(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(1);
        IDomainObject result = loadedMap.get(id);
        if (result == null) {
            result = doLoad(id, resultSet);
            loadedMap.put(id, result);
        }
        return result;
    }

    abstract protected IDomainObject doLoad(int id, ResultSet resultSet) throws SQLException;
}
