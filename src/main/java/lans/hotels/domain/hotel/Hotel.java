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
            Integer hotel_group_id, String name, String email,
            Address address, String contact, String city, Integer pin_code) {
        super(id, dataSource);
        this.hotel_group_id = hotel_group_id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.contact = contact;
        this.city = city;
        this.pin_code = pin_code;
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

    public void setHotel_group_id(Integer hotel_group_id) {
        this.hotel_group_id = hotel_group_id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setName(Address address) {
        this.address = address;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setPincode(Integer pin_code) {
        this.pin_code = pin_code;
    }

public void remove() throws UoWException {
        markRemoved();
    }
}
