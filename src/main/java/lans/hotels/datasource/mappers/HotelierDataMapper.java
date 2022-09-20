package lans.hotels.datasource.mappers;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.search_criteria.AbstractSearchCriteria;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.user_types.Hotelier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
    public ArrayList<Hotelier> findAll() throws SQLException {
        String findAllStatment =
            "SELECT ho.id AS id, name, email, password, role, user_id, is_active " +
            "FROM app_user u  " +
            "JOIN hotelier ho " +
            "ON u.id = ho.user_id ";
        try (PreparedStatement statement = connection.prepareStatement(findAllStatment)) {
            ResultSet resultSet = statement.executeQuery();
            Hotelier currentHotelier = load(resultSet);
            while (currentHotelier != null) {
                currentHotelier = load(resultSet);
            }
            return new ArrayList<>(loadedMap.values());
        } catch (UoWException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Hotelier> findBySearchCriteria(AbstractSearchCriteria criteria) throws Exception {
        return null;
    }

    @Override
    protected Hotelier doLoad(Integer id, ResultSet rs) throws SQLException {
        Hotelier hotelier = new Hotelier(rs.getInt("user_id"), dataSource,rs.getInt("id"),
                rs.getString("name"),rs.getString("email"),
                rs.getString("password"),rs.getInt("role"),
                rs.getBoolean("is_active"));

        return hotelier;
    }



    @Override
    public Hotelier update(AbstractDomainObject domainObject) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
