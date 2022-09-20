package lans.hotels.domain.user_types;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.domain.IDataSource;

public class Hotelier extends User {

    int id;
    int userID;
    boolean isActive;

    public Hotelier(Integer id, IDataSource dataSource, String name, String email, String password, Integer role, int userID, boolean isActive) {
        super(id,dataSource);
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isActive = isActive;
        this.userID = userID;
        initMaps();
    }

    public Boolean getStatus()
    {
        return this.isActive;
    }

    public void setStatus() throws UoWException {
        this.isActive = isActive;
        markDirty();
    }

    public int getUserID()
    {
        return this.userID;
    }

    private void initMaps() {
    }

    public int getID()
    {
        return this.id;
    }

}
