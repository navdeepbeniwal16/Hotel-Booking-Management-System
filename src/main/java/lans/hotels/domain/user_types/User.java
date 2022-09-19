package lans.hotels.domain.user_types;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.ReferenceObject;

public class User extends ReferenceObject {

    int id;
    String name;
    String email;
    String password;
    int role;

    public User(IDataSource dataSource) throws UoWException {
        super(dataSource);
        initMaps();
        markNew();
    }

    public User(Integer id, IDataSource dataSource) throws UoWException {
        super(id, dataSource);
        initMaps();
        markClean();
    }

    public User(Integer id, IDataSource dataSource, String name, String email, String password, Integer role) {
        super(id, dataSource);
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        initMaps();
    }

    public int getID()
    {
        return this.id;
    }

    public String getName()
    {
        return this.email;
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

    public void setPassword(String password) throws UoWException {
        this.password = password;
        markDirty();
    }

    public Integer getRole()
    {
        return this.role;
    }

    private void initMaps() {
    }



}
