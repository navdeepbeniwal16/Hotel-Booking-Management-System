package lans.hotels.domain.user_types;
import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.search_criteria.BookingsSearchCriteria;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.booking.Booking;
import lans.hotels.domain.utils.Address;

import java.util.ArrayList;

public class Customer extends User{
    Address address;
    String contact;
    int age;
    boolean isActive;

    public Customer(IDataSource dataSource) throws UoWException {
        super(dataSource);
        initMaps();
        markNew();
    }

    public Customer(Integer id, IDataSource dataSource) throws UoWException {
        super(id, dataSource);
        initMaps();
        markClean();
    }

    public Customer(Integer id,IDataSource dataSource,String name, String email,
                    String password, Integer role, Address address, String contact, Integer age,Boolean isActive) throws Exception {
        super(id,dataSource);
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = new Role(role);
        this.address = address;
        this.contact = contact;
        this.age = age;
        this.isActive = isActive;
        initMaps();
    }

    public Customer(IDataSource dataSource,String name, String email,
                    String password, Integer role, Address address, String contact, Integer age,Boolean isActive) throws Exception {
        super(dataSource);
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = new Role(role);
        this.address = address;
        this.contact = contact;
        this.age = age;
        this.isActive = isActive;
        initMaps();
    }

    private void initMaps() {}

    public void setAddress(Address address) throws UoWException {
        this.address = address;
        markDirty();
    }
    public Address getAddress() { return this.address; }

    public void setContact(String contact) throws UoWException {
        this.contact = contact;
        markDirty();}

    public String getContact() { return this.contact; }

    public void setAge(int contact) throws UoWException {
        this.age = age;
        markDirty();
    }
    public int getAge() { return this.age; }

    public Boolean getStatus()
    {
        return this.isActive;
    }

    public void setStatus() throws UoWException {
        this.isActive = isActive;
        markDirty();
    }

    public void remove() throws UoWException {
        markRemoved();
    }

    public ArrayList<Booking> getAllBookings() {
        BookingsSearchCriteria bookingsSearchCriteria = new BookingsSearchCriteria();
        bookingsSearchCriteria.setCustomerId(this.getId());
        ArrayList<Booking> allBookings = new ArrayList<>();
        try {
            allBookings = dataSource.findBySearchCriteria(Booking.class, bookingsSearchCriteria);
            System.out.println("In Customer : Number of bookings fetched : " + allBookings.size());
        } catch (Exception e) {
            System.out.println("Exception occured while fetching bookings for the customer");
            System.out.println(e.getMessage());
        }

        return allBookings;
    }

    @Override public String toString() {
        return "Customer(" + id + ")";
    }
}
