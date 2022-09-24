package lans.hotels.domain.hotel_group;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.search_criteria.BookingsSearchCriteria;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.ReferenceObject;
import lans.hotels.domain.utils.Address;

import lans.hotels.domain.booking.Booking;

import java.util.ArrayList;


public class HotelGroup extends ReferenceObject{
    String name;
    Address address;
    String phone;


    public HotelGroup(IDataSource dataSource) throws UoWException {
        super(dataSource);
        initMaps();
        markNew();
    }

    public HotelGroup(Integer id, IDataSource dataSource) throws UoWException {
        super(id, dataSource);
        initMaps();
        markClean();
    }

    public HotelGroup(Integer id, IDataSource dataSource, String name) throws UoWException {
        super(id, dataSource);
        this.name = name;
        initMaps();
        markClean();
    }


    public HotelGroup(Integer id,
                      IDataSource dataSource,
                      String name,
                      Address address,
                      String phone) throws UoWException {
        super(id, dataSource);
        this.name = name;
        this.address = address;
        this.phone = phone;
        initMaps();
    }

    public HotelGroup(IDataSource dataSource, String name, Address address, String phone) throws UoWException {
        super(dataSource);
        this.name = name;
        this.address = address;
        this.phone = phone;
        initMaps();
    }

    public Integer getId() {
        return id;
    }
    public String getName() {
        return this.name;
    }
    public Address getAddress() {
        return this.address;
    }
    public String getPhone() {
        return this.phone;
    }

    public void setName(String name) throws UoWException {
        this.name = name;
        markDirty();
    }
    public void setAddress(Address address) throws UoWException {
        this.address = address;
        markDirty();
    }
    public void setPhone(String phone) throws UoWException {
        this.phone = phone;
        markDirty();
    }

    public void remove() throws UoWException {
        markRemoved();
    }

    private void initMaps() {
    }

    public ArrayList<Booking> getAllBookings() {
        BookingsSearchCriteria bookingsSearchCriteria = new BookingsSearchCriteria();
        bookingsSearchCriteria.setHotelGroupId(this.getId());
        ArrayList<Booking> allBookings = new ArrayList<>();
        try {
            allBookings = dataSource.findBySearchCriteria(Booking.class, bookingsSearchCriteria);
            System.out.println("In Hotel Group : Number of bookings fetched : " + allBookings.size());
        } catch (Exception e) {
            System.out.println("Exception occurred while fetching bookings for the hotel group");
            System.out.println(e.getMessage());
        }

        return allBookings;
    }

}

