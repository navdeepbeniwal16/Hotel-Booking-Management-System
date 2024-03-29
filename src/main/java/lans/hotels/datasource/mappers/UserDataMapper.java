package lans.hotels.datasource.mappers;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.search_criteria.AbstractSearchCriteria;
import lans.hotels.datasource.search_criteria.UserSearchCriteria;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.user.Role;
import lans.hotels.domain.user.User;
import lans.hotels.domain.utils.Address;
import lans.hotels.domain.utils.District;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDataMapper extends AbstractPostgresDataMapper<User> {

    public UserDataMapper(Connection connection, IDataSource dataSource) {
        super(connection, "app_user", dataSource);
        idPrefix = "u.";
    }
    @Override
    protected String findStatement() {
        String statement =
                "SELECT u.id AS id, u.name AS name, u.email, u.version AS version, line_1 AS address_l1, line_2 AS address_l2, \n" +
                "d.name AS district_name, postcode, city, r.id AS rid, r.name AS role_name, contact, age, \n" +
                "hg.id AS hotelier_hotel_group_id, hg.name AS hotelier_hotel_group_name\n" +
                "FROM app_user u \n" +
                "LEFT JOIN roles r on u.role = r.id \n" +
                "LEFT JOIN ( address a LEFT JOIN district d ON a.district = d.id ) ON u.address = a.id \n" +
                "LEFT JOIN ( hotel_group_hotelier hgh JOIN hotel_group hg ON hg.id = hgh.hotel_group_id ) ON hgh.hotelier_id = u.id \n";
        return statement;
    }

    @Override
    protected String insertStatement() {
        return null;
    }

    @Override
    public ArrayList<User> findAll() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(findStatement())) {
            ResultSet resultSet = statement.executeQuery();
            while (load(resultSet) != null) {
                load(resultSet);
            }
            return new ArrayList<>(loadedMap.values());
        } catch (UoWException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<User> constructUsersFromResultSet(ResultSet resultSet) throws SQLException {
        ArrayList<User> users = new ArrayList<>();


        return users;
    }

    @Override
    public ArrayList<User> findBySearchCriteria(AbstractSearchCriteria criteria){
        UserSearchCriteria userSearchCriteria = (UserSearchCriteria) criteria;
        String findByStatement = findStatement();

        if (userSearchCriteria.getId() != null){
            findByStatement += "WHERE u.id = '" + userSearchCriteria.getId() + "'";
        }
        else if (userSearchCriteria.getEmail() != null){
            findByStatement += "WHERE u.email = '" + userSearchCriteria.getEmail() + "' ";
        }
        else if (userSearchCriteria.getRole() != null){
            findByStatement += "WHERE u.role = '" + userSearchCriteria.getRole().getId() + "'";
        }
        else if (userSearchCriteria.getHotelierHotelGroupID() != null){
            findByStatement += "WHERE hg.id = '" + userSearchCriteria.getHotelierHotelGroupID() + "'";
        }

        try (PreparedStatement statement = connection.prepareStatement(findByStatement)) {
            ResultSet resultSet = statement.executeQuery();
            while (load(resultSet) != null) {
                load(resultSet);
            }
            return new ArrayList<>(loadedMap.values());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    protected User doLoad(Integer id, ResultSet rs) throws Exception {

        User user = null;
        Integer uid;
        String name;
        String email;
        String address_l1;
        String address_l2;
        String district_name;
        String city;
        Integer postcode;
        Integer role_id;
        String contact;
        Integer age;
        Integer hotelier_hotel_group_id;
        String hotelier_hotel_group_name;
        Integer version;

        uid = rs.getInt("id");
        name = rs.getString("name");
        email = rs.getString("email");
        address_l1 = rs.getString("address_l1");
        address_l2 = rs.getString("address_l2");
        district_name = rs.getString("district_name");
        role_id = rs.getInt("rid");
        city = rs.getString("city");
        postcode = rs.getInt("postcode");
        contact = rs.getString("contact");
        age = rs.getInt("age");
        hotelier_hotel_group_id = rs.getInt("hotelier_hotel_group_id");
        hotelier_hotel_group_name = rs.getString("hotelier_hotel_group_name");
        version = rs.getInt("version");

        District district = new District(district_name);
        Address address = new Address(address_l1,
                address_l2,
                district,
                city,
                postcode);
        Role role = new Role(role_id);

        try {
            user = new User(
                    uid,
                    dataSource,
                    name,
                    email,
                    address,
                    role,
                    contact,
                    age,
                    hotelier_hotel_group_id,
                    hotelier_hotel_group_name,
                    version
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public User update(AbstractDomainObject domainObject) throws Exception {
        User user = (User) domainObject;
        Integer version = user.getVersion();
        Integer new_version = version +1;
        String updatedName = " SET version = "+new_version+" , name='" + user.getName() + "'";
        String updatedEmail = ", email='" + user.getEmail() + "'";
        String updatedRole = ", role=" + user.getRole().getId();
        Integer id = user.getId() != null ?  user.getId() : -1;
        String updateStatement = "UPDATE app_user " +
                updatedName +
                updatedEmail +
                updatedRole +
                " WHERE id = " + id + " AND version = "+version+
                " RETURNING *";

        PreparedStatement statement = connection.prepareStatement(updateStatement);
        ResultSet resultSet = statement.executeQuery();
        assert resultSet.getFetchSize() != 0;
        return user;
    }

    @Override
    public <DomainObject extends AbstractDomainObject> Integer insert(DomainObject domainObject) throws SQLException, UoWException {
        User u = (User) domainObject;
        PreparedStatement statement = connection.prepareStatement("INSERT INTO " +
                "app_user(name,email,address,role,contact,age) " +
                "VALUES " +
                "(NULL,?,NULL,?,NULL,NULL) returning * ");


        statement.setString(1,u.getEmail());
        statement.setInt(2,u.getRole().getId());

        ResultSet resultSet = statement.executeQuery();
        return resultSet.next() ? resultSet.getInt("id") : -1;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
