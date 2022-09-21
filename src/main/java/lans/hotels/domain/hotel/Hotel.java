package lans.hotels.domain.hotel;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.ReferenceObject;
import lans.hotels.domain.room.Room;
import lans.hotels.domain.room.RoomSpecification;
import lans.hotels.domain.utils.Address;
import lans.hotels.domain.utils.Phone;

import java.util.HashMap;

public class Hotel extends ReferenceObject {
    int hotel_group_id;
    String name;
    String email;
    Address address;
    String contact;
    String city;
    int pin_code;
    boolean is_active;

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

    public Hotel(Integer id, IDataSource dataSource,
            Integer hotel_group_id, String name, String email,Address address,
                 String contact, String city, Integer pin_code, boolean is_active) {
        super(id, dataSource);
        this.hotel_group_id = hotel_group_id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.contact = contact;
        this.city = city;
        this.pin_code = pin_code;
        this.is_active = is_active;
        initMaps();
    }

    public Hotel(IDataSource dataSource,
                 Integer hotel_group_id, String name, String email,Address address,
                 String contact, String city, Integer pin_code, boolean is_active) {
        super(dataSource);
        this.hotel_group_id = hotel_group_id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.contact = contact;
        this.city = city;
        this.pin_code = pin_code;
        this.is_active = is_active;
        initMaps();
    }

    private void initMaps() {
        this.rooms = new HashMap<>();
        this.roomSpecifications = new HashMap<>();
    }

    public int getID() {
        return id;
    }
    public int getHotelGroupID() {
        return this.hotel_group_id;
    }
    public String getName() {
        return this.name;
    }
    public String getEmail() {
        return this.email;
    }
    public Address getAddress() {
        return this.address;
    }
    public String getContact() {
        return this.contact;
    }
    public String getCity() {
        return this.city;
    }
    public int getPinCode() {
        return this.pin_code;
    }
    public boolean getIsActive() {
        return this.is_active;
    }

    public void setName(String name) throws UoWException {
        this.name = name;
        markDirty();
    }
    public void setEmail(String email) throws UoWException {
        this.email = email;
        markDirty();
    }
    public void setAddress(Address address) throws UoWException {
        this.address = address;
        markDirty();
    }
    public void setContact(String contact) throws UoWException {
        this.contact = contact;
        markDirty();
    }
    public void setCity(String city) throws UoWException {
        this.city = city;
        markDirty();
    }
    public void setPincode(Integer pin_code) throws UoWException {
        this.pin_code = pin_code;
        markDirty();
    }

    public void setIs_Active(Boolean is_active) throws UoWException {
        this.is_active = is_active;
        markDirty();
    }

    public void remove() throws UoWException {
            markRemoved();
        }
}
