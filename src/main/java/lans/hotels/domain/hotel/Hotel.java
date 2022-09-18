package lans.hotels.domain.hotel;

import lans.hotels.datasource.exceptions.UoWException;
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
    String city;

    HashMap<Integer, Room> rooms;
    HashMap<Integer, RoomSpecification> roomSpecifications;

    public Hotel(IDataSource dataSource) throws UoWException {
        super(dataSource);
        initMaps();
        markNew();
    }

    public Hotel(Integer id, IDataSource dataSource) throws UoWException {
        super(id, dataSource);
        initMaps();
        markClean();
    }

    protected Hotel(String name, Phone phone, String email, Integer id, IDataSource dataSource) {
        super(id, dataSource);
        this.name = name;
        this.phone = phone;
        this.email = email;
        initMaps();
    }

    public void setPhone(Phone phone) throws UoWException {
        this.phone = phone;
        markDirty();
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

    public void remove() throws UoWException {
        markRemoved();
    }
}
