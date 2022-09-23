package lans.hotels.domain.room;

import lans.hotels.domain.IDataSource;
import lans.hotels.domain.ReferenceObject;
import lans.hotels.domain.hotel.Hotel;

public class Room extends ReferenceObject {
    private Hotel hotel;
    private int roomNumber;
    private int roomFloor;
    private boolean isActive;
    private Integer hotelId;

    protected Room(Hotel hotel,
                   IDataSource dataSource) {
        super(dataSource);
        this.hotel = hotel;
    }

    public Room(Hotel hotel,
                int roomNumber,
                int roomFloor,
                boolean isActive,
                Integer id,
                IDataSource dataSource) {
        super(id, dataSource);
        this.hotel = hotel;
        this.roomNumber = roomNumber;
        this.roomFloor = roomFloor;
        this.isActive = isActive;
    }

    public Hotel getHotel() { return this.hotel; }

    public int getRoomFloor() {
        return roomFloor;
    }

    public int getRoomNumber() {
        return roomNumber;
    }


    @Override
    public String toString() {
        return "Room(" + id + ")";
    }
}
