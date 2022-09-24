package lans.hotels.domain.user_types;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.hotel_group.HotelGroup;

public class Hotelier extends User {

    int hotelierID;
    boolean isActive;
    HotelGroup hotelGroup;

    public Hotelier(IDataSource dataSource) throws UoWException {
        super(dataSource);
        initMaps();
        markNew();
    }

    public Hotelier(Integer id, IDataSource dataSource) throws UoWException {
        super(id, dataSource);
        initMaps();
        markClean();
    }

    public Hotelier(Integer userID,
                    IDataSource dataSource,
                    Integer id,
                    String name,
                    String email,
                    String password,
                    Integer role,
                    boolean isActive) {
        super(userID,dataSource);
        this.hotelierID = id;
        this.isActive = isActive;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        hotelGroup = null;
        initMaps();
    }

    public Hotelier(IDataSource dataSource,
                    String name,
                    String email,
                    String password,
                    Integer role,
                    boolean isActive) {
        super(dataSource);
        this.isActive = isActive;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        initMaps();
    }

    public Hotelier(Integer userID, IDataSource dataSource, Integer id, boolean isActive){
        super(userID,dataSource);
        this.hotelierID = id;
        this.isActive = isActive;
    }

    private void initMaps() {}

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

    public int getHotelierID()
    {
        return this.hotelierID;
    }

    public void remove() throws UoWException {
        markRemoved();
    }

    public HotelGroup getHotelGroup() {
        return hotelGroup;
    }

    public void setHotelGroup(HotelGroup hotelGroup) {
        this.hotelGroup = hotelGroup;
        // TODO: mark dirty?
    }
}
