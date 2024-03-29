package lans.hotels.datasource.mappers;
import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.search_criteria.AbstractSearchCriteria;
import lans.hotels.datasource.search_criteria.HotelGroupHotelierSearchCriteria;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.hotel_group.HotelGroupHotelier;

import java.sql.*;
import java.util.ArrayList;

public class HotelGroupHotelierDataMapper extends AbstractPostgresDataMapper<HotelGroupHotelier> {

    public HotelGroupHotelierDataMapper(Connection connection, IDataSource dataSource) {
        super(connection, "hotel_group_hotelier", dataSource);
        idPrefix = "hgh.";
    }
    @Override
    protected String findStatement() {
        String statement =
                "SELECT * FROM hotel_group_hotelier hgh ";
        return statement;
    }

    @Override
    protected String insertStatement() {
        return null;
    }

    @Override
    public ArrayList<HotelGroupHotelier> findAll() throws SQLException {
        return null;
    }



    @Override
    public ArrayList<HotelGroupHotelier> findBySearchCriteria(AbstractSearchCriteria criteria){
        HotelGroupHotelierSearchCriteria hgh_criteria = (HotelGroupHotelierSearchCriteria) criteria;
        String findAllStatement = findStatement();

        if (hgh_criteria.getHotelierID() != null){
            findAllStatement += "WHERE hgh.hotelier_id = '" + hgh_criteria.getHotelierID() + "'";
        }

        try (PreparedStatement statement = connection.prepareStatement(findAllStatement)) {
            ResultSet resultSet = statement.executeQuery();
            HotelGroupHotelier currenthgh = load(resultSet);
            while (currenthgh != null) {
                currenthgh = load(resultSet);
            }
            return new ArrayList<>(loadedMap.values());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected HotelGroupHotelier doLoad(Integer id, ResultSet rs) throws Exception {
        HotelGroupHotelier hgh = new HotelGroupHotelier(
                id,
                dataSource,
                rs.getInt("hotelier_id"),
                rs.getInt("hotel_group_id"));
        return hgh;
    }

    @Override
    public HotelGroupHotelier update(AbstractDomainObject domainObject) {
        return null;
    }

    @Override
    public <DomainObject extends AbstractDomainObject> Integer insert(DomainObject domainObject) throws Exception {
        HotelGroupHotelier hgh = (HotelGroupHotelier) domainObject;
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO " +
                "hotel_group_hotelier(hotelier_id,hotel_group_id) " +
                "VALUES " +
                "(?,?) returning * ");


        statement.setInt(1,hgh.getHotelierID());
        statement.setInt(2,hgh.getHotelGroupID());

        ResultSet resultSet = statement.executeQuery();

        if(resultSet.next())
        {
            return resultSet.getInt("id");
        }
        else
            throw new Exception("Insert failed");
    }

    @Override
    public boolean delete(Integer id) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "DELETE FROM hotel_group_hotelier WHERE id = ? returning * ");
            statement.setInt(1,id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
            {
                return true;
            }
            else
                try {
                    throw new Exception("Delete failed");
                } catch (Exception e) {
                    e.printStackTrace();
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
