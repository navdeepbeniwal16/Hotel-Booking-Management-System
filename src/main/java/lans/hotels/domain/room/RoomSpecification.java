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
    Integer price;

    public RoomSpecification(IDataSource dataSource) throws UoWException {
        super(dataSource);
        markNew();
    }

    public RoomSpecification(Integer id, IDataSource dataSource) throws UoWException {
        super(id, dataSource);
        markClean();
        // TODO: #ghost
    }
    public RoomSpecification(Integer id,
                             Integer hotelId,
                             Integer occupancy,
                             String bedType,
                             String roomType,
                             Integer price,
                             IDataSource dataSource) throws UoWException {
        super(id, dataSource);
        this.hotelId = hotelId;
        this.roomType = roomType;
        this.bedType = bedType;
        this.occupancy = occupancy;
        this.price = price;
        markClean();
    }

    public String getDescription() { return description; }
    public Integer getOccupancy() { return occupancy; }
    public String getRoomType() { return roomType; }

    public Integer getHotelId() {return hotelId;}

    public Integer getPrice() {return price;}

    public String getBedType() {return bedType;}


    // TODO: #concurrency warning - this may cause concurrency issues
}
