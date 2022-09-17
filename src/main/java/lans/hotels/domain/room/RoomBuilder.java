package lans.hotels.domain.room;

import lans.hotels.domain.IDataSource;
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
    private Integer id;
    private IDataSource dataSource;

    public RoomBuilder(Hotel hotel,
                       RoomSpecification specification,
                       IDataSource dataSource) {
        this.dataSource = dataSource;
        this.hotel = hotel;
        this.specification = specification;
        reset();
    }

    public RoomBuilder id(Integer id) {
        this.id = id;
        return this;
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

    @Override
    public void reset() {
        this.isActive = true;
        this.roomFloor = 0;
        this.roomNumber = 0;
    }

    @Override
    public Room getResult() {
        if (this.room == null) {
            this.room = new Room(hotel, specification, roomNumber, roomFloor, isActive, id, dataSource);
        }
        return this.room;
    }
}
