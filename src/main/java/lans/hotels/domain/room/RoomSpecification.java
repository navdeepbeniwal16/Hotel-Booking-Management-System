package lans.hotels.domain.room;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.ReferenceObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RoomSpecification extends ReferenceObject {
    Integer hotelId;
    String roomType;
    String bedType;
    Integer occupancy;

    String description;
    Double price;
    ArrayList<String> features;

    public RoomSpecification(IDataSource dataSource) throws UoWException {
        super(dataSource);
        markNew();
    }

    public RoomSpecification(Integer id, IDataSource dataSource) throws UoWException {
        super(id, dataSource);
        markClean();
        // TODO: #ghost
    }
    private RoomSpecification(Integer id,
                              Integer hotelId,
                              Integer occupancy,
                              String bedType,
                              String roomType,
                              String description,
                              ArrayList<String> features,
                              Double price,
                              IDataSource dataSource) throws UoWException {
        super(id, dataSource);
        this.hotelId = hotelId;
        this.roomType = roomType;
        this.bedType = bedType;
        this.occupancy = occupancy;
        this.description = description;
        this.price = price;
        this.features = features;
        markClean();
    }

    public String getDescription() { return description; }
    public Integer getOccupancy() { return occupancy; }
    public ArrayList<String> getFeatures() { return features; }
    public String getRoomType() { return roomType; }

    public Integer getHotelId() {return hotelId;}

    public Double getPrice() {return price;}

    public String getBedType() {return bedType;}


    // TODO: #concurrency warning - this may cause concurrency issues
    public void addFeature(String feature) {
        if (!features.contains(feature)) features.add(feature);
    }
}
