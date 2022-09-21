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
                "SELECT * FROM app_user u ";
        System.out.println("HotelDataMapper.findStatement(): " + statement);
        return statement;
    }

    @Override
    protected String insertStatement() {
        return null;
    }

    @Override
    public ArrayList<User> findAll() throws SQLException {
        return null;
    }

    @Override
    public ArrayList<User> insert() throws Exception {
        return null;
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
    public <DomainObject extends AbstractDomainObject> Boolean create(DomainObject domainObject) throws SQLException, UoWException {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
