package lans.hotels.domain.hotel_group;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.ReferenceObject;
import lans.hotels.domain.user_types.Role;


public class HotelGroupHotelier extends ReferenceObject{
    String name;
    String email;
    Role role;
    String hotel_group;


    public HotelGroupHotelier(IDataSource dataSource) {
        super(dataSource);
        try {
            markNew();
        } catch (UoWException e) {
            e.printStackTrace();
        }
    }

    public HotelGroupHotelier(Integer id, IDataSource dataSource) {
        super(id, dataSource);
        try {
            markClean();
        } catch (UoWException e) {
            e.printStackTrace();
        }
    }

    public HotelGroupHotelier(Integer id,
                              IDataSource dataSource,
                              String name,
                              String email,
                              Role role,
                              String hotel_group) throws Exception {
        super(id, dataSource);
        this.name = name;
        this.email = email;
        this.role = role;
        this.hotel_group = hotel_group;
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

    public void setEmail(String email) throws UoWException {
        this.email = email;
        markDirty();
    }

    public Role getRole() {
        return this.role;
    }

    public String getHotelGroup() {
        return hotel_group;
    }

    public void setHotelGroup(String hotel_group) throws UoWException {
        this.hotel_group = hotel_group;
        markDirty();
    }

    public void remove() throws UoWException {
        markRemoved();
    }

    private void initMaps() {
    }
}

