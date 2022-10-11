package lans.hotels.domain.user;

public class Role {
    public enum Name {
        Admin,
        Hotelier,
        Customer
    }

    Integer id;
    String name;

    public static Role admin() {
        return new Role(1);
    }
    public static Role hotelier() {
        return new Role(2);
    }


    public static Role customer() {
        return new Role(3);
    }

    public Role() {
        this.id = 3;
        name = Name.Customer.toString();
    }

    public Role(Integer id) {
        assert id == 1 || id == 2 || id == 3;
        this.id = id;
        if (id==1) {
            name = Name.Admin.toString();
        }
        if (id==2) {
            name = Name.Hotelier.toString();
        }
        name = Name.Customer.toString();
    }

    public boolean isAdmin() { return id == 1; }
    public boolean isHotelier() { return id == 2; }
    public boolean isCustomer() { return id == 3; }

    public String getName() {
        return this.name;
    }

    public Integer getId() {
        return id;
    }

}
