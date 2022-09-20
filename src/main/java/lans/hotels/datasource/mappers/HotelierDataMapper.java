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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Hotelier> insert() throws Exception {
        return null;
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
    public <DomainObject extends AbstractDomainObject> Boolean create(DomainObject domainObject) throws Exception {
        {
            Hotelier hotelier = (Hotelier) domainObject;
            String createStatement = "WITH insert_app_user AS ( " +
                    "INSERT INTO app_user (name,email,password,role) " +
                    "VALUES ( " + "'" + hotelier.getName() + "'," +
                    "'" + hotelier.getEmail() + "'," +
                    "'" + hotelier.getPassword() + "'," +
                    "'" + hotelier.getRole() + "') " +
                    "RETURNING id " +
                    ") " +
                    "INSERT INTO hotelier (user_id, is_active) " +
                    "VALUES " +
                    "((SELECT id FROM insert_app_user),True) " +
                    "returning * ";
            System.out.println(createStatement);
            try (PreparedStatement statement = connection.prepareStatement(createStatement)) {
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next())
                    return true;
                else return false;

            }
        }
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
