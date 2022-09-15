package lans.hotels.domain.room;

import lans.hotels.domain.ReferenceObject;
import lans.hotels.domain.IBuilder;
import lans.hotels.domain.hotel.Hotel;

public class RoomBuilder implements IBuilder<Room> {
    Integer uid;
    Room room;
    private Hotel hotel;
    private int roomNumber;
    private int roomFloor;
    private boolean isActive;
    private RoomSpecification specification;

    public RoomBuilder(Hotel hotel, RoomSpecification specification) {
        this.hotel = hotel;
        this.specification = specification;
        reset();
    }

    public RoomBuilder number(int roomNumber) {
        this.roomNumber = roomNumber;
        return this;
    }

    public RoomBuilder floor(int floorNumber) {
        this.roomFloor = floorNumber;
        return this;
    }

    public RoomBuilder active(boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public RoomBuilder specification(RoomSpecification specification) {
        this.specification = specification;
        return this;
    }

    @Override
    public void reset() {
        this.isActive = true;
        this.roomFloor = 0;
        this.roomNumber = 0;
    }

    @Override
    public Room getResult() {
        if (this.room == null) {
            this.room = new Room(this.hotel, this.uid, this.roomNumber, this.roomFloor, this.isActive);
        }
        if (this.specification != null) this.room.setSpecification(specification);
        return this.room;
    }
}
