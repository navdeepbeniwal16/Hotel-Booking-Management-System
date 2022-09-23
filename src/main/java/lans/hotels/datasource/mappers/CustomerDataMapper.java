package lans.hotels.datasource.mappers;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.search_criteria.AbstractSearchCriteria;
import lans.hotels.datasource.search_criteria.CustomerSearchCriteria;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.user_types.Customer;
import lans.hotels.domain.user_types.Hotelier;
import lans.hotels.domain.utils.District;
import lans.hotels.domain.utils.Address;

import java.sql.*;
import java.util.ArrayList;

public class CustomerDataMapper extends AbstractPostgresDataMapper<Customer> {
    private static final String COLUMNS = " number, floor, is_active  ";

    public CustomerDataMapper(Connection connection, IDataSource dataSource) {
        super(connection, "customer", dataSource);
    }
    @Override
    protected String findStatement() {
        String statement =
                "SELECT customer_id as id, name, email, password, role, " +
                        "contact, age, line_1 as address_l1,line_2 AS address_l2, " +
                        "district_name, postcode, city, is_active " +
                        "FROM app_user u " +
                        "JOIN ( " +
                        "SELECT user_id,c.id AS customer_id, contact, age, is_active," +
                        " line_1, line_2, city, postcode, name as district_name " +
                        "FROM customer c " +
                        "JOIN ( " +
                        "address a JOIN district d ON a.district = d.id " +
                        ") " +
                        "ON c.address = a.id " +
                        ") AS cc " +
                        "ON u.id = cc.user_id ";
        return statement;
    }

    @Override
    protected String insertStatement() {
        return null;
    }

    @Override
    public ArrayList<Customer> findAll() throws SQLException {
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

    @Override
    public ArrayList<Customer> findBySearchCriteria(AbstractSearchCriteria criteria) throws Exception {
        CustomerSearchCriteria customerSearchCriteria = (CustomerSearchCriteria) criteria;
        String findByCriteriaStatement = findStatement();

        System.out.println(findByCriteriaStatement);
        if(customerSearchCriteria.getCustomerId()!=null) {
            findByCriteriaStatement += "WHERE customer_id ='" + customerSearchCriteria.getCustomerId() + "'";
        }

        if (customerSearchCriteria.getName() != null){
            findByCriteriaStatement += "WHERE name = '" + customerSearchCriteria.getName() + "'";
        }

        if (customerSearchCriteria.getEmail() != null){
            findByCriteriaStatement += "WHERE email = '" + customerSearchCriteria.getEmail() + "'";
        }

        if (customerSearchCriteria.getIsActive() != null){
            findByCriteriaStatement += "WHERE is_active = '" + customerSearchCriteria.getIsActive() + "'";
        }

        try (PreparedStatement statement = connection.prepareStatement(findByCriteriaStatement)) {
            ResultSet resultSet = statement.executeQuery();
            Customer loadedCustomer = load(resultSet);
            while (loadedCustomer != null) {
                loadedCustomer = load(resultSet);
            }
            return new ArrayList<>(loadedMap.values());
        } catch (Exception e) {
            System.out.println("Exception occurred at findBySearchCriteria");
            e.printStackTrace();
        }

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
                        rs.getString("contact"),rs.getInt("age"),rs.getBoolean("is_active"));
        return customer;
    }

    @Override
    public Customer update(AbstractDomainObject domainObject) {
        return null;
    }

    @Override
    public <DomainObject extends AbstractDomainObject> Boolean insert(DomainObject domainObject){
        Customer customer = (Customer) domainObject;
        String createStatement = "WITH insert_address AS ( " +
                "        INSERT INTO address (line_1,line_2,district,city,postcode) " +
                "        VALUES " +
                "                ('1-15','College Cres',(SELECT id from district WHERE name = 'NSW'),'Melbourne',3052) " +
                "RETURNING id " +
                "), " +
                "insert_app_user AS ( " +
                "        INSERT INTO app_user (name,email,password,role) VALUES ( '" + customer.getName() + "',"
                + "'" + customer.getEmail() + "'," + "'" + customer.getPassword() + "', 3 )" +
                "RETURNING id ) " +
                "INSERT INTO customer (user_id,address,contact,age,is_active) " +
                "VALUES ((SELECT id FROM insert_app_user),(SELECT id FROM insert_address), '" + customer.getContact() + "'," +
                customer.getAge() + ",true) returning * ";
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

    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
