package lans.hotels.datasource.mappers;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.search_criteria.AbstractSearchCriteria;
import lans.hotels.datasource.search_criteria.UserSearchCriteria;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.user_types.User;

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
                "SELECT * FROM app_user u";
        System.out.println("HotelDataMapper.findStatement(): " + statement);
        return statement;
    }

    @Override
    protected String insertStatement() {
        return null;
    }

    @Override
    public ArrayList<User> findAll() throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        String findAllQuery = "SELECT u.id AS uid, u.name, u.email, r.id AS rid" +
                " FROM app_user u " +
                " LEFT JOIN roles r on u.role = r.id; ";
        try(PreparedStatement statement = connection.prepareStatement(findAllQuery)) {
            ResultSet resultSet = statement.executeQuery();

            Integer uid;
            String name;
            String email;
            Integer roleId;
            while(resultSet.next()) {
                uid = resultSet.getInt("uid");
                name = resultSet.getString("name");
                email = resultSet.getString("email");
                roleId = resultSet.getInt("rid");
                users.add(new User(uid, dataSource, name, email, roleId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }


    @Override
    public ArrayList<User> findBySearchCriteria(AbstractSearchCriteria criteria){
        UserSearchCriteria userSearchCriteria = (UserSearchCriteria) criteria;
        String findAllStatement = findStatement();

        if (userSearchCriteria.getId() != null){
            findAllStatement += "WHERE u.id = '" + userSearchCriteria.getId() + "'";
            System.out.println(findAllStatement);
            System.out.println("ID passed to HotelDataMapper : " + userSearchCriteria.getId());
        }


        try (PreparedStatement statement = connection.prepareStatement(findAllStatement)) {
            ResultSet resultSet = statement.executeQuery();
            User currentUser = load(resultSet);
            while (currentUser != null) {
                currentUser = load(resultSet);
            }
            return new ArrayList<>(loadedMap.values());
        } catch (Exception e) {
            System.out.println("Exception occurred at findBySearchCriteria");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected User doLoad(Integer id, ResultSet rs) throws SQLException {

        User user = new User(id,dataSource);
        return user;
    }

    @Override
    public User update(AbstractDomainObject domainObject) {

        return null;
    }

    @Override
    public <DomainObject extends AbstractDomainObject> Boolean insert(DomainObject domainObject) throws SQLException, UoWException {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
