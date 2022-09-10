package lans.hotels.datasource.mappers;


import lans.hotels.domain.IDomainObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import java.sql.Connection;
public abstract class AbstractPostgresMapper<DomainObjectImp extends IDomainObject> implements IDataMapper<DomainObjectImp> {
    protected Connection connection;
    protected Map<Integer, DomainObjectImp> loadedMap = new HashMap();
    abstract protected String findStatement();

    protected AbstractPostgresMapper(Connection connection) {
        this.connection = connection;
    }

    protected DomainObjectImp abstractGetById(int id) throws SQLException {
        DomainObjectImp result = loadedMap.get(id);
        if (result == null) {
            result = getFromDb(id);
        }
        return result;
    }

    private DomainObjectImp getFromDb(int id) throws SQLException {
        try (PreparedStatement findStatement = connection.prepareStatement(findStatement())){
            findStatement.setInt(1, id);
            ResultSet resultSet = findStatement.executeQuery();
            resultSet.next();
            return load(resultSet);
        }
    }

    protected DomainObjectImp load(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(1);
        DomainObjectImp result = loadedMap.get(id);
        if (result == null) {
            result = doLoad(id, resultSet);
            loadedMap.put(id, result);
        }
        return result;
    }

    abstract protected DomainObjectImp doLoad(int id, ResultSet resultSet) throws SQLException;
    abstract public DomainObjectImp getById(int id);
}
