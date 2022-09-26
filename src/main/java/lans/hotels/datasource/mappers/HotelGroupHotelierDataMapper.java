package lans.hotels.datasource.mappers;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.search_criteria.AbstractSearchCriteria;
import lans.hotels.datasource.search_criteria.HotelGroupHotelierSearchCriteria;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.hotel_group.HotelGroup;
import lans.hotels.domain.hotel_group.HotelGroupHotelier;
import lans.hotels.domain.user_types.Role;
import lans.hotels.domain.user_types.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HotelGroupHotelierDataMapper extends AbstractPostgresDataMapper<HotelGroupHotelier> {

    public HotelGroupHotelierDataMapper(Connection connection, IDataSource dataSource) {
        super(connection, "hotel_group", dataSource);
        idPrefix = "h.";
    }
    @Override
    protected String findStatement() {
        String statement =
                "SELECT u.id AS id, u.name,email,r.id AS role_id,hg.name AS hotel_group_name from app_user u " +
                "LEFT JOIN roles r on u.role = r.id " +
                "LEFT JOIN ( hotel_group_hotelier hgh LEFT JOIN hotel_group hg on hgh.hotel_group_id = hg.id) " +
                "ON u.id = hgh.hotelier_id " +
                "WHERE r.id = 2 ";
        System.out.println("HotelGroupHotelierDataMapper.findStatement(): " + statement);
        return statement;
    }

    @Override
    protected String insertStatement() {
        return null;
    }

    @Override
    public ArrayList<HotelGroupHotelier> findAll() throws SQLException {
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
    public ArrayList<HotelGroupHotelier> findBySearchCriteria(AbstractSearchCriteria criteria){
        HotelGroupHotelierSearchCriteria hgCriteria = (HotelGroupHotelierSearchCriteria) criteria;
        String findAllStatement = findStatement();

        if (hgCriteria.getHotelGroupID() != null){
            findAllStatement += "AND hgh.hotel_group_id = '" + hgCriteria.getHotelGroupID() + "'";
            System.out.println("Find by criteria statement : \n "+findAllStatement);
            System.out.println("HotelGroupID passed to HotelGroupHotelierDataMapper : " + hgCriteria.getHotelGroupID());
        }

        try (PreparedStatement statement = connection.prepareStatement(findAllStatement)) {
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
    protected HotelGroupHotelier doLoad(Integer id, ResultSet rs) throws Exception {
        HotelGroupHotelier hg_hotelier = null;
        Integer uid;
        String name;
        String email;
        Integer role_id;
        String hg;

        uid = rs.getInt("id");
        name = rs.getString("name");
        email = rs.getString("email");
        role_id = rs.getInt("role_id");
        hg = rs.getString("hotel_group_name");

        Role role = new Role(role_id);

        try {
            hg_hotelier = new HotelGroupHotelier(uid,
                    dataSource,
                    name,
                    email,
                    role,
                    hg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hg_hotelier;
    }

    @Override
    public HotelGroupHotelier update(AbstractDomainObject domainObject) {
        return null;
    }

    @Override
    public <DomainObject extends AbstractDomainObject> Boolean insert(DomainObject domainObject) throws SQLException, UoWException {
        HotelGroupHotelier hgHotelier = (HotelGroupHotelier) domainObject;
        String createStatement =null;
//
//
//        String createStatement = "INSERT INTO hotel_group_hotelier (hotelier_id,hotel_group_id) VALUES ("
//                + hgHotelier.getHotelierId() + "," + hgHotelier.getHotelGroupId() + ") returning * ";

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
        String deleteStatement = "DELETE FROM hotel_group_hotelier WHERE id = " + id +" returning * ";

        System.out.println(deleteStatement);
        try (PreparedStatement statement = connection.prepareStatement(deleteStatement)) {
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        }
        catch (SQLException e)
        {
            return false;
        }
    }
}
