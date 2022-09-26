package lans.hotels.domain.user_types;

public class Role {
    private enum RoleNames {
        Admin,
        Hotelier,
        Customer
    }

    Integer id;
    String name;

    public Role(Integer id) throws Exception {
        if (id!=1 && id!=2 && id!=3) throw new Exception("invalid user role id: " + id);
        if (id==1) name = RoleNames.Admin.toString();
        if (id==2) name = RoleNames.Hotelier.toString();
        if (id==3) name = RoleNames.Customer.toString();
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Integer getId() {
        return this.id;
    }

}
