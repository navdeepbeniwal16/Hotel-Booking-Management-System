package lans.hotels.domain.hotel;

import lans.hotels.domain.Entity;

public class Room extends Entity {
    private int roomNumber;
    private int roomFloor;
    private boolean isActive;
    private int specificationId;

    public Room(int id, int specificationId, int roomNumber, int roomFloor, boolean isActive) {
        super(id);
        this.specificationId = specificationId;
        this.roomNumber = roomNumber;
        this.roomFloor = roomFloor;
        this.isActive = isActive;
    }

    public int getRoomFloor() {
        return roomFloor;
    }

    public int getRoomNumber() {
        return roomNumber;
    }
}
