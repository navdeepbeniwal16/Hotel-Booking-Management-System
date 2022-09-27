package lans.hotels.domain.user_types;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.datasource.search_criteria.BookingsSearchCriteria;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.ReferenceObject;
import lans.hotels.domain.booking.Booking;
import lans.hotels.domain.utils.Address;

import java.util.ArrayList;

public class User extends ReferenceObject {

    String name;
    String email;
    Address address;
    Role role;
    String contact;
    Integer age;

    public User(IDataSource dataSource){
        super(dataSource);
        try {
            markNew();
        } catch (UoWException e) {
            e.printStackTrace();
        }
    }

    public User(Integer id, IDataSource dataSource){
        super(id, dataSource);
        try {
            markClean();
        } catch (UoWException e) {
            e.printStackTrace();
        }
    }

    public User(IDataSource dataSource,
                String name,
                String email,
                Address address,
                Role role,
                String contact,
                Integer age
    ) throws Exception {
        super(dataSource);
        this.name = name;
        this.email = email;
        this.address = address;
        this.role = role;
        this.contact = contact;
        this.age = age;
        markNew();
        markLoaded();
    }

    public User(Integer id,
                IDataSource dataSource,
                String name,
                String email,
                Address address,
                Role role,
                String contact,
                Integer age
                ) throws Exception {
        super(id, dataSource);
        this.name = name;
        this.email = email;
        this.address = address;
        this.role = role;
        this.contact = contact;
        this.age = age;
        markClean();
        markLoaded();
    }

    public int getID()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name) throws UoWException {
        this.name = name;
        markDirty();
    }

    public String getEmail()
    {
        return this.email;
    }

    public void setEmail(String email) throws UoWException {
        this.email = email;
        markDirty();
    }

    public Address getAddress() { return this.address; }

    public void setAddress(Address address) throws UoWException {
        this.address = address;
        markDirty();
    }

    public Integer getRoleId()
    {
        return this.role.id;
    }

    public String getRoleName() {
        return role.name;
    }

    public String getContact() { return this.contact; }

    public void setContact(String contact) throws UoWException {
        this.contact = contact;
        markDirty();}

    public int getAge() { return this.age; }

    public void setAge(int age) throws UoWException {
        this.age = age;
        markDirty();
    }

    public ArrayList<Booking> getAllBookings() {
        BookingsSearchCriteria bookingsSearchCriteria = new BookingsSearchCriteria();
        bookingsSearchCriteria.setCustomerId(this.getId());
        ArrayList<Booking> allBookings = new ArrayList<>();
        try {
            allBookings = dataSource.findBySearchCriteria(Booking.class, bookingsSearchCriteria);
            System.out.println("Users : Number of bookings fetched : " + allBookings.size());
        } catch (Exception e) {
            System.out.println("Exception occured while fetching bookings for the customer");
            System.out.println(e.getMessage());
        }

        return allBookings;
    }

    private void initMaps() {
    }

    @Override public String toString() {
        return "User(" + id + ")";
    }
}
