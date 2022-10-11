package lans.hotels.domain.user;

public class Role {
    public enum Name {
        Admin,
        Hotelier,
        Customer
    }

    Integer id;
    String name = "none";

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

    public Role(Integer rid) {
        assert rid == 1 || rid == 2 || rid == 3;
        id = rid;
        if (id==1) name = Name.Admin.toString();
        if (id==2) name = Name.Hotelier.toString();
        if (id==3) name = Name.Customer.toString();
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
