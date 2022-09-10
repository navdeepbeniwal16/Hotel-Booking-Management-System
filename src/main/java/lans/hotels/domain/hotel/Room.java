package lans.hotels.domain.hotel;

import lans.hotels.domain.Entity;

public class Room extends Entity {
    private Hotel hotel;
    private int roomNumber;
    private int roomFloor;
    private boolean isActive;
    private int specificationId;

    // TODO: refactor to better creational pattern - façade?
    public static Room createNewRoom(Hotel hotel, int specificationId, int roomNumber, int roomFloor) {
        return new Room(hotel, specificationId, roomNumber, roomFloor);
    }

    public Room(Hotel hotel, int id, int specificationId, int roomNumber, int roomFloor, boolean isActive) {
        super(id);
        this.hotel = hotel;
        this.specificationId = specificationId;
        this.roomNumber = roomNumber;
        this.roomFloor = roomFloor;
        this.isActive = isActive;
    }

    private Room(Hotel hotel, int specificationId, int roomNumber, int roomFloor) {
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
}
