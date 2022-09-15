package lans.hotels.domain.hotel;

import lans.hotels.domain.IDataSource;
import lans.hotels.domain.ReferenceObject;
import lans.hotels.domain.room.Room;
import lans.hotels.domain.room.RoomSpecification;
import lans.hotels.domain.utils.Phone;

import java.util.HashMap;

public class Hotel extends ReferenceObject {
    private String name;
    Phone phone;
    String email; // TODO: refactor email into value object.
    String address;

    HashMap<Integer, Room> rooms;
    HashMap<Integer, RoomSpecification> roomSpecifications;

    public Hotel(IDataSource dataSource) {
        super(dataSource);
        initMaps();
    }

    public Hotel(Integer id, IDataSource dataSource) {
        super(id, dataSource);
        initMaps();
    }

    protected Hotel(String name, Phone phone, String email, Integer id, IDataSource dataSource) {
        super(id, dataSource);
        this.name = name;
        this.phone = phone;
        this.email = email;
        initMaps();
    }

    private void initMaps() {
        this.rooms = new HashMap<>();
        this.roomSpecifications = new HashMap<>();
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }

    public void setAddress(String address) { this.address = address; }
    public String getAddress() { return this.address; }
    public Phone getPhone() { return this.phone; }
}
