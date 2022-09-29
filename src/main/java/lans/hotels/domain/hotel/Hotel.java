package lans.hotels.domain.hotel;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.search_criteria.BookingsSearchCriteria;
import lans.hotels.datasource.search_criteria.RoomBookingSearchCriteria;
import lans.hotels.datasource.search_criteria.RoomSearchCriteria;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.ReferenceObject;
import lans.hotels.domain.booking.Booking;
import lans.hotels.domain.booking.RoomBooking;
import lans.hotels.domain.room.Room;
import lans.hotels.domain.utils.Address;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Hotel extends ReferenceObject {
    Integer hotel_group_id;
    String name;
    String email;
    Address address;
    String contact;
    String city;
    int pin_code;
    boolean is_active;
    ArrayList<Room> rooms;

    public Hotel(Integer id, IDataSource dataSource, Integer hotel_group_id) throws UoWException {
        super(id, dataSource);
        this.hotel_group_id = hotel_group_id;
        markClean();
        markGhost();
    }

    public Hotel(IDataSource dataSource,
                 Integer hotel_group_id,
                 String name, String email,Address address,
                 String contact, String city, Integer pin_code, boolean is_active) throws UoWException {
        super(dataSource);
        this.hotel_group_id = hotel_group_id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.contact = contact;
        this.city = city;
        this.pin_code = pin_code;
        this.is_active = is_active;
        markNew();
        markLoaded();
    }

    public Hotel(Integer id, IDataSource dataSource,
            Integer hotel_group_id, String name, String email,Address address,
                 String contact, String city, Integer pin_code, boolean is_active) throws UoWException {
        super(id, dataSource);
        this.hotel_group_id = hotel_group_id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.contact = contact;
        this.city = city;
        this.pin_code = pin_code;
        this.is_active = is_active;
        markClean();
        markLoaded();
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

    public ArrayList<Room> getAllRooms() {
        RoomSearchCriteria criteria = new RoomSearchCriteria();
        criteria.setHotelId(this.getID());
        try {
            this.rooms = dataSource.findBySearchCriteria(Room.class, criteria);
        } catch (Exception e) {
            System.out.println("Exception occurred in Hotel: getAllRooms()");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return rooms;
    }

    public ArrayList<Room> getAllUnAvailableRooms(Date startDate, Date endDate) {
        ArrayList<Room> unAvailableRooms = new ArrayList<>();
        BookingsSearchCriteria bookingsSearchCriteria = new BookingsSearchCriteria();
        bookingsSearchCriteria.setHotelId(this.getID());
        bookingsSearchCriteria.setStartDate(startDate);
        bookingsSearchCriteria.setEndDate(endDate);
        try {
            ArrayList<Booking> bookings = dataSource.findBySearchCriteria(Booking.class, bookingsSearchCriteria);
            for(Booking booking: bookings) {
                Map<Integer, RoomBooking> roomBookingMap = booking.getRoomBookings();
                if(roomBookingMap.isEmpty()) throw new RuntimeException("No room bookings are available for Booking with id : " + booking.getId());

                for(Object key: roomBookingMap.keySet()) {
                    RoomBooking rb = roomBookingMap.get(key);
                    for(Room room: getAllRooms()) {
                        if(rb.getRoomId() == room.getId()) {
                            unAvailableRooms.add(room);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return unAvailableRooms;
    }

    public ArrayList<Room> getAllAvailableRooms(Date startDate, Date endDate) {
        ArrayList<Room> allRooms =  getAllRooms();
        ArrayList<Room> unAvailableRooms = getAllUnAvailableRooms(startDate, endDate);
        ArrayList<Room> availableRooms = new ArrayList<>();
        for(Room room: allRooms) {
            boolean isUnAvailable = false;
            for(Room unRoom: unAvailableRooms) {
                if(room.getId() == unRoom.getId()) {
                    isUnAvailable = true;
                    break;
                }
            }
            if(!isUnAvailable) availableRooms.add(room);
        }
        return availableRooms;
    }

    public void remove() throws UoWException {
            markRemoved();
    }
}
