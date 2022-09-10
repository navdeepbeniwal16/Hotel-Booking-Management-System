package lans.hotels.domain.hotel;

import lans.hotels.domain.Entity;

public class Room extends Entity {
    int roomNumber;
    int roomFloor;
    boolean isActive;
    int specificationId;

    public Room(int id, int specificationId, int roomNumber, int roomFloor, boolean isActive) {
        super(id);
        this.specificationId = specificationId;
        this.roomNumber = roomNumber;
        this.roomFloor = roomFloor;
        this.isActive = isActive;
    }
}
