package lans.hotels.domain.room;

import lans.hotels.domain.AbstractReferenceObject;
import lans.hotels.domain.hotel.Hotel;

public class Room extends AbstractReferenceObject {
    private Hotel hotel;
    private int roomNumber;
    private int roomFloor;
    private boolean isActive;
    private int specificationId;
    private RoomSpecification specification;

    public Room(Hotel hotel, int id, int specificationId, int roomNumber, int roomFloor, boolean isActive) {
        super(id);
        this.hotel = hotel;
        this.specificationId = specificationId;
        this.roomNumber = roomNumber;
        this.roomFloor = roomFloor;
        this.isActive = isActive;
    }

    public Room(Hotel hotel, int specificationId, int roomNumber, int roomFloor) {
        super();
        this.hotel = hotel;
        this.specificationId = specificationId;
        this.roomNumber = roomNumber;
        this.roomFloor = roomFloor;
        this.isActive = true;
    }

    public Hotel getHotel() { return this.hotel; }

    public int getRoomFloor() {
        return roomFloor;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public int getSpecificationId() {
        return specificationId;
    }
    public RoomSpecification getSpecification() { return specification; }
    public void setSpecification(RoomSpecification newSpec) {
        this.specification = newSpec;
        this.specificationId = newSpec.getUid();
    }
}
