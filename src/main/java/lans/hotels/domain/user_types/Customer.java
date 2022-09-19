package lans.hotels.domain.user_types;
import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.utils.Address;

public class Customer extends User{
    Address address;
    String contact;
    int age;

    public Customer(Integer id,IDataSource dataSource,String name, String email, String password, Integer role, Address address, String contact, int age) {
        super(id,dataSource);
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.address = address;
        this.contact = contact;
        this.age = age;
    }
    public Customer(IDataSource dataSource) throws UoWException {
        super(dataSource);
        markNew();
    }

    public Customer(Integer id, IDataSource dataSource) throws UoWException {
        super(id, dataSource);
        markClean();
    }

    public void setAddress(Address address) { this.address = address; }
    public Address getAddress() { return this.address; }

    public void setContact(String contact) { this.contact = contact; }
    public String getContact() { return this.contact; }

    public void setAge(int contact) { this.age = age; }
    public int getAge() { return this.age; }


    public void remove() throws UoWException {
        markRemoved();
    }

    @Override public String toString() {
        return "Customer(" + id + ")";
    }
}
