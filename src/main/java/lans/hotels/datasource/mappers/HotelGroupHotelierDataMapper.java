package lans.hotels.datasource.mappers;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.search_criteria.AbstractSearchCriteria;
import lans.hotels.datasource.search_criteria.HotelGroupHotelierSearchCriteria;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.hotel_group.HotelGroupHotelier;

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
                "SELECT * FROM hotel_group_hotelier ";
        System.out.println("HotelGroupDataMapper.findStatement(): " + statement);
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
            HotelGroupHotelier aHotelGroupHotelier = load(resultSet);
            while (aHotelGroupHotelier != null) {
                System.out.println(aHotelGroupHotelier);
                aHotelGroupHotelier = load(resultSet);
            }
        } catch (Exception e) {
            System.err.println("ERROR HotelGroupHotelierDataMapper.findAll(): " + e);
            e.printStackTrace();
        } finally {
            System.out.println("HotelGroupHotelierDataMapper.findAll() - loaded hotel groups: " + loadedMap);
        }
        return new ArrayList<>(loadedMap.values());
    }



    @Override
    public ArrayList<HotelGroupHotelier> findBySearchCriteria(AbstractSearchCriteria criteria){
        HotelGroupHotelierSearchCriteria hgCriteria = (HotelGroupHotelierSearchCriteria) criteria;
        String findAllStatement = findStatement();

        if (hgCriteria.getHotelGroupID() != null){
            findAllStatement += "WHERE h.id = '" + hgCriteria.getHotelGroupID() + "'";
            System.out.println("ID passed to HotelGroupHotelierDataMapper : " + hgCriteria.getHotelGroupID());
        }

        try (PreparedStatement statement = connection.prepareStatement(findAllStatement)) {
            ResultSet resultSet = statement.executeQuery();
            HotelGroupHotelier currentHgHotelier = load(resultSet);
            while (currentHgHotelier != null) {
                currentHgHotelier = load(resultSet);
            }
            return new ArrayList<>(loadedMap.values());
        } catch (Exception e) {
            System.out.println("Exception occurred at findBySearchCriteria");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected HotelGroupHotelier doLoad(Integer id, ResultSet rs) throws SQLException, UoWException {
        HotelGroupHotelier hotelGroup = new HotelGroupHotelier(id,dataSource,
                rs.getInt("hotelier_id"),rs.getInt("hotel_group_id"));
        return hotelGroup;
    }

    @Override
    public HotelGroupHotelier update(AbstractDomainObject domainObject) {
        return null;
    }

    @Override
    public <DomainObject extends AbstractDomainObject> Boolean insert(DomainObject domainObject) throws SQLException, UoWException {
        HotelGroupHotelier hgHotelier = (HotelGroupHotelier) domainObject;
        String createStatement = "INSERT INTO hotel_group_hotelier (hotelier_id,hotel_group_id) VALUES ("
                + hgHotelier.getHotelierId() + "," + hgHotelier.getHotelGroupId() + ") returning * ";

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
