package lans.hotels.datasource.mappers;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.search_criteria.AbstractSearchCriteria;
import lans.hotels.datasource.search_criteria.HotelGroupSearchCriteria;
import lans.hotels.datasource.search_criteria.HotelierSearchCriteria;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.hotel_group.HotelGroup;
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
        String statement =
                "SELECT ho.id AS id, name, email, password, role, user_id, is_active " +
                        "FROM app_user u  " +
                        "JOIN hotelier ho " +
                        "ON u.id = ho.user_id ";
        return statement;
    }

    @Override
    protected String insertStatement() {
        return null;
    }

    @Override
    public ArrayList<Hotelier> findAll() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(findStatement())) {
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
    public ArrayList<Hotelier> findBySearchCriteria(AbstractSearchCriteria criteria) throws Exception {
        HotelierSearchCriteria hCriteria = (HotelierSearchCriteria) criteria;
        String findAllStatement = findStatement();

        if (hCriteria.getId() != null){
            findAllStatement += "WHERE ho.id = '" + hCriteria.getId() + "'";
        }

        if (hCriteria.getName() != null){
            findAllStatement += "WHERE name = '" + hCriteria.getName() + "'";
        }

        if (hCriteria.getEmail() != null){
            findAllStatement += "WHERE email = '" + hCriteria.getEmail() + "'";
        }

        if (hCriteria.getIsActive() != null){
            findAllStatement += "WHERE is_active = '" + hCriteria.getIsActive() + "'";
        }

        try (PreparedStatement statement = connection.prepareStatement(findAllStatement)) {
            ResultSet resultSet = statement.executeQuery();
            Hotelier currentHotelier = load(resultSet);
            while (currentHotelier != null) {
                currentHotelier = load(resultSet);
            }
            return new ArrayList<>(loadedMap.values());
        } catch (Exception e) {
            e.printStackTrace();
        }

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
    public <DomainObject extends AbstractDomainObject> Boolean insert(DomainObject domainObject) throws Exception {
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
            catch (SQLException e)
            {
                return false;
            }
        }
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
