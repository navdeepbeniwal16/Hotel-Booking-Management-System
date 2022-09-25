package lans.hotels.domain.user_types;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.ReferenceObject;

public class User extends ReferenceObject {

    String name;
    String email;
    String password;

    Role role;

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

    public User(Integer id,
                IDataSource dataSource,
                String name,
                String email,
                Integer role) throws Exception {
        super(id, dataSource);
        this.name = name;
        this.email = email;
        this.role = new Role(role);
        try {
            markClean();
        } catch (UoWException e) {
            e.printStackTrace();
        }
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
    public String getPassword()
    {
        return this.password;
    }

    public void setEmail(String email) throws UoWException {
        this.email = email;
        markDirty();
    }

    public void setPassword(String password) throws UoWException {
        this.password = password;
        markDirty();
    }

    public Integer getRoleId()
    {
        return this.role.id;
    }

    public String getRoleName() {
        return role.name;
    }

    private void initMaps() {
    }
}
