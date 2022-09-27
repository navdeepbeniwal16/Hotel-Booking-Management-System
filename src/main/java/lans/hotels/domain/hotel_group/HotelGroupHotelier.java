package lans.hotels.domain.hotel_group;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.ReferenceObject;


public class HotelGroupHotelier extends ReferenceObject{
    Integer hotelier_id;
    Integer hotel_group_id;


    public HotelGroupHotelier(IDataSource dataSource) throws UoWException{
        super(dataSource);
        markNew();
    }

    public HotelGroupHotelier(Integer id, IDataSource dataSource) throws UoWException{
        super(id, dataSource);
        markClean();
    }

    public HotelGroupHotelier(IDataSource dataSource,
                              Integer hotelier_id,
                              Integer hotel_group_id) throws UoWException {
        super(dataSource);
        this.hotelier_id = hotelier_id;
        this.hotel_group_id = hotel_group_id;
        markNew();
        markLoaded();
    }

    public HotelGroupHotelier(Integer id,
                              IDataSource dataSource,
                              Integer hotelier_id,
                              Integer hotel_group_id) throws UoWException {
        super(id, dataSource);
        this.hotelier_id = hotelier_id;
        this.hotel_group_id = hotel_group_id;
        markClean();
        markLoaded();
    }

    public int getID()
    {
        return this.id;
    }

    public Integer getHotelGroupID()
    {
        return this.hotel_group_id;
    }

    public void setHotelGroupID(Integer hotel_group_id) throws UoWException {
        this.hotel_group_id = hotel_group_id;
        markDirty();
    }

    public Integer getHotelierID()
    {
        return this.hotelier_id;
    }

    public void setHotelierID(Integer hotelier_id) throws UoWException {
        this.hotelier_id = hotelier_id;
        markDirty();
    }

    public void remove() throws UoWException {
        markRemoved();
    }

    private void initMaps() {
    }
}

