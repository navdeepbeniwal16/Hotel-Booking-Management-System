package lans.hotels.datasource.mappers;

import lans.hotels.datasource.search_criteria.AbstractSearchCriteria;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.user_types.User;

import java.sql.*;
import java.util.ArrayList;

public class UserDataMapper extends AbstractPostgresDataMapper<User> {

    public UserDataMapper(Connection connection, IDataSource dataSource) {
        super(connection, "app_user", dataSource);
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
    public User doCreate(User domainObject){
//        String createStatment = "INSERT INTO " + this.table +
//                "(user_id,is_active) VALUES (" + domainObject.getUserID() +",TRUE) returning *";
//        try (PreparedStatement statement = connection.prepareStatement(createStatment)) {
//            ResultSet resultSet = statement.executeQuery();
//            User currentUser = load(resultSet);
//            return currentUser;
//        }
        return null;
    }

    @Override
    public ArrayList<User> findAll() throws SQLException {
        String findAllStatment = "SELECT " + " * " +
                " FROM " + this.table;
        System.out.println(findAllStatment);
        try (PreparedStatement statement = connection.prepareStatement(findAllStatment)) {
            ResultSet resultSet = statement.executeQuery();

            User currentUser = load(resultSet);
            while (currentUser != null) {
                currentUser = load(resultSet);
            }
            return new ArrayList<>(loadedMap.values());
        }
    }

    @Override
    public ArrayList<User> findBySearchCriteria(AbstractSearchCriteria criteria) throws Exception {
        return null;
    }

    @Override
    protected User doLoad(Integer id, ResultSet rs) throws SQLException {
        User user = new User(rs.getInt("id"), dataSource, rs.getString("name"),rs.getString("email"),rs.getString("password"), rs.getInt("role"));
        return user;
    }

    @Override
    public User update(AbstractDomainObject domainObject) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
