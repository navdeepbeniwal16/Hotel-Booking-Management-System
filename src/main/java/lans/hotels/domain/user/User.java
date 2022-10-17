package lans.hotels.domain.user;

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
    Role role = new Role();
    String contact;
    Integer age;
    Integer hotelier_hotel_group_id;
    String hotelier_hotel_group_name;

    public User(IDataSource dataSource, String email) throws UoWException {
        super(dataSource);
        this.email = email;
        markNew();
    }

    public static User newCustomer(IDataSource dataSource, String email) throws UoWException {
        return new User(dataSource, email, Role.customer());
    }

    public static User newHotelier(IDataSource dataSource, String email) throws UoWException {
        return new User(dataSource, email, Role.hotelier());
    }

    public static User newAdmin(IDataSource dataSource, String email) throws UoWException {
        return new User(dataSource, email, Role.admin());
    }

    private User(IDataSource dataSource, String email, Role role) throws UoWException {
        super(dataSource);
        this.email = email;
        this.role = role;
        markNew();
    }

    public User(Integer id, IDataSource dataSource) throws UoWException {
        super(id, dataSource);
        markClean();
    }

    public User(IDataSource dataSource,
                String name,
                String email,
                Address address,
                Role role,
                String contact,
                Integer age,
                Integer hotelier_hotel_group_id,
                String hotelier_hotel_group_name
                ) throws UoWException {
        super(dataSource);
        this.name = name;
        this.email = email;
        this.address = address;
        this.role = role;
        this.contact = contact;
        this.age = age;
        this.hotelier_hotel_group_id = hotelier_hotel_group_id;
        this.hotelier_hotel_group_name = hotelier_hotel_group_name;
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
                Integer age,
                Integer hotelier_hotel_group_id,
                String hotelier_hotel_group_name
                ) throws Exception {
        super(id, dataSource);
        this.name = name;
        this.email = email;
        this.address = address;
        this.role = role;
        this.contact = contact;
        this.age = age;
        this.hotelier_hotel_group_id = hotelier_hotel_group_id;
        this.hotelier_hotel_group_name = hotelier_hotel_group_name;
        markClean();
        markLoaded();
    }

    public String getName()
    {
        if (this.name != null) return this.name;
        return "";
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

    public boolean hasRole() { return this.role != null; }
    public Role getRole() { return this.role; }


    public void setRole(Role role) throws UoWException {
        this.role = role;
        markDirty();
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

    public Integer getHotelierHotelGroupID() {
        if (this.hotelier_hotel_group_id != null) return this.hotelier_hotel_group_id;
        return -1;
    }

    public void setHotelierHotelGroupID(Integer hotelier_hotel_group_id) throws UoWException {
        this.hotelier_hotel_group_id = hotelier_hotel_group_id;
        markDirty();
    }

    public String getHotelierHotelGroupName() { return this.hotelier_hotel_group_name; }

    public void getHotelierHotelGroupName(String hotelier_hotel_group_name) throws UoWException {
        this.hotelier_hotel_group_name = hotelier_hotel_group_name;
        markDirty();
    }

    public ArrayList<Booking> getAllBookings() {
        BookingsSearchCriteria bookingsSearchCriteria = new BookingsSearchCriteria();
        bookingsSearchCriteria.setCustomerId(this.getId());
        ArrayList<Booking> allBookings = new ArrayList<>();
        try {
            allBookings = dataSource.findBySearchCriteria(Booking.class, bookingsSearchCriteria);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allBookings;
    }

    private void initMaps() {
    }

    @Override public String toString() {
        return "User(" + id + ")";
    }
}
