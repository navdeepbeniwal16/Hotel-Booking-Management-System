package lans.hotels.datasource.mappers;

import lans.hotels.domain.IDataSource;
import lans.hotels.domain.user_types.Hotelier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HotelierDataMapper extends AbstractPostgresDataMapper<Hotelier> {

    public HotelierDataMapper(Connection connection, IDataSource dataSource) {
        super(connection, "hotelier", dataSource);
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
    public Hotelier doCreate(Hotelier domainObject) {
        return null;
    }

    @Override
    public List<Hotelier> findAll() throws SQLException {
        String findAllStatment = "SELECT " + " * " +
                " FROM " + this.table;
        try (PreparedStatement statement = connection.prepareStatement(findAllStatment)) {
            ResultSet resultSet = statement.executeQuery();
            Hotelier currentHotelier = load(resultSet);
            while (currentHotelier != null) {
                currentHotelier = load(resultSet);
            }
            return new ArrayList<>(loadedMap.values());
        }
    }

    @Override
    protected Hotelier doLoad(Integer id, ResultSet rs) throws SQLException {
        Hotelier hotelier = new Hotelier(rs.getInt("id"), dataSource, rs.getInt("user_id"), rs.getBoolean("id"));
        return hotelier;
    }

    @Override
    public Hotelier update(Hotelier domainObject) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
