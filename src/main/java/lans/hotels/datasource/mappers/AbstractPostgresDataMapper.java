package lans.hotels.datasource.mappers;


import lans.hotels.datasource.facade.IDataMapper;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.sql.Connection;
public abstract class AbstractPostgresDataMapper<DomainObject extends AbstractDomainObject>
        implements IDataMapper<DomainObject> {
    protected IDataSource dataSource;
    protected Connection connection;
    protected String table;
    protected Map<Integer, DomainObject> loadedMap; // TODO: unfuck #bug
    abstract protected String findStatement();
    abstract protected String insertStatement();
    protected abstract DomainObject doLoad(Integer id, ResultSet resultSet) throws Exception;
    //public abstract DomainObject create(DomainObject domainObject);
    public abstract ArrayList<DomainObject> findAll() throws SQLException;

    protected String idPrefix;

    protected AbstractPostgresDataMapper(Connection connection, String table, IDataSource dataSource) {
        this.connection = connection;
        this.table = table;
        this.dataSource = dataSource;
        this.loadedMap = new HashMap();
        idPrefix = "";
    }

    protected void printAllResultSetColumnNames(ResultSet resultSet) throws SQLException {
        for(int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
            System.out.println(i + ". " + resultSet.getMetaData().getColumnName(i));
        }
    }

    public DomainObject getById(Integer id) {
        try {
            return getFromDb(id);
        } catch (Exception e) {
            System.err.println("AbstractPostgresDataMapper.getById(): " + e.getMessage());
            // TODO: do not return null!
            return null;
        }
    }

    private DomainObject getFromDb(Integer id) throws Exception {
        try (PreparedStatement findStatement = connection.prepareStatement(findStatement())){
            findStatement.setInt(1, id);
            ResultSet resultSet = findStatement.executeQuery();
            return load(resultSet);
        }
    }
    protected DomainObject load(ResultSet resultSet) throws Exception {
        if (!resultSet.next()) return null; // TODO: bug? refactor - see getAll()

        // TODO: abstract out type to generic?

        Integer id = resultSet.getInt("id");
        DomainObject result = loadedMap.get(id);
        if (result == null) {
            result = doLoad(id, resultSet);
            loadedMap.put(id, result);
        }
        return result;
    }
}
