package lans.hotels.domain.user_types;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.domain.IDataSource;

public class Hotelier extends User {

    int hotelierID;
    int userID;
    boolean isActive;

    public Hotelier(Integer userID, IDataSource dataSource, Integer id, String name, String email,
                    String password, Integer role, boolean isActive) {
        super(userID,dataSource);
        this.hotelierID = id;
        this.isActive = isActive;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
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
        return super.id;
    }

    private void initMaps() {
    }

    public int getHotelierID()
    {
        return this.hotelierID;
    }

}
