package lans.hotels.domain.room;

import lans.hotels.domain.ReferenceObject;
import lans.hotels.domain.hotel.Hotel;

public class Room extends ReferenceObject {
    private Hotel hotel;
    private int roomNumber;
    private int roomFloor;
    private boolean isActive;
    private RoomSpecification specification;

    protected Room(Hotel hotel,
                   int id,
                   int roomNumber,
                   int roomFloor,
                   boolean isActive) {
        super(id);
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

    public RoomSpecification getSpecification() { return specification; }
    public void setSpecification(RoomSpecification newSpec) {
        this.specification = newSpec;
    }
}
