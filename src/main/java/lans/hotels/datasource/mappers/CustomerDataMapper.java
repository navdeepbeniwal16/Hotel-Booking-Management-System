package lans.hotels.datasource.mappers;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.search_criteria.AbstractSearchCriteria;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.user_types.Customer;
import lans.hotels.domain.utils.District;
import lans.hotels.domain.utils.Address;

import java.sql.*;
import java.util.ArrayList;

public class CustomerDataMapper extends AbstractPostgresDataMapper<Customer> {
    private static final String COLUMNS = " number, floor, is_active, room_spec_id ";

    public CustomerDataMapper(Connection connection, IDataSource dataSource) {
        super(connection, "customer", dataSource);
    }
    @Override
    protected String findStatement() {return null;}

    @Override
    protected String insertStatement() {
        return null;
    }

    @Override
    public Customer doCreate(Customer domainObject) {
        return null;
    }

    @Override
    public ArrayList<Customer> findAll() throws SQLException {
        String findAllStatement = "SELECT customer_id as id, name, email, password, role, " +
                "contact, age, line_1 as address_l1,line_2 AS address_l2, " +
                "district_name, postcode, city " +
                "FROM app_user u " +
                "JOIN ( " +
                "SELECT user_id,c.id AS customer_id, contact, age, line_1, line_2, city, postcode, name as district_name " +
                "FROM customer c " +
                "JOIN ( " +
                "address a JOIN district d ON a.district = d.id " +
                ") " +
                "ON c.address = a.id " +
                ") AS cc " +
                "ON u.id = cc.user_id ";

//        String findAllStatement = "SELECT h.id AS id, contact, age, line_1, line_2, city, postcode, name FROM " + this.table +" h JOIN ( address a JOIN district d ON a.district = d.id) ON h.address = a.id";
        try (PreparedStatement statement = connection.prepareStatement(findAllStatement)) {
            ResultSet resultSet = statement.executeQuery();
            while (load(resultSet) != null) {
                load(resultSet);
            }
            return new ArrayList<>(loadedMap.values());
        } catch (UoWException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Customer> findBySearchCriteria(AbstractSearchCriteria criteria) throws Exception {
        return null;
    }

    @Override
    protected Customer doLoad(Integer id, ResultSet rs) throws SQLException {
        District district = new District(rs.getString("district_name"));
        Address address = new Address(rs.getString("address_l1"),
                rs.getString("address_l2"),
                district,
                rs.getString("city"),
                rs.getInt("postcode"));

        Customer customer =
                new Customer(id,dataSource,rs.getString("name"),rs.getString("email"),
                        rs.getString("password"),rs.getInt("role"),address,
                        rs.getString("contact"),rs.getInt("age"));
        return customer;
    }

    @Override
    public Customer update(AbstractDomainObject domainObject) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
