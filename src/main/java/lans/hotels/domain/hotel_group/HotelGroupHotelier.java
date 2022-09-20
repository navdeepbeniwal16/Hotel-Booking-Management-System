package lans.hotels.domain.hotel_group;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.ReferenceObject;
import lans.hotels.domain.utils.Address;


public class HotelGroupHotelier extends ReferenceObject{
    int hotelier_id;
    int hotel_group_id;


    public HotelGroupHotelier(IDataSource dataSource) throws UoWException {
        super(dataSource);
        initMaps();
        markNew();
    }

    public HotelGroupHotelier(Integer id, IDataSource dataSource) throws UoWException {
        super(id, dataSource);
        initMaps();
        markClean();
    }

    public HotelGroupHotelier(Integer id, IDataSource dataSource,
                      Integer hotelier_id, Integer hotel_group_id) throws UoWException {
        super(id, dataSource);
        this.hotelier_id = hotelier_id;
        this.hotel_group_id = hotel_group_id;
        initMaps();
    }

    public HotelGroupHotelier(IDataSource dataSource, Integer hotelier_id, Integer hotel_group_id) {
        super(dataSource);
        this.hotelier_id = hotelier_id;
        this.hotel_group_id = hotel_group_id;
        initMaps();
    }

    public Integer getId() {
        return id;
    }
    public Integer getHotelierId() {
        return hotelier_id;
    }
    public Integer getHotelGroupId() {
        return hotel_group_id;
    }

    public void setHotelierID(Integer hotelier_id) throws UoWException {
        this.hotelier_id = hotelier_id;
        markDirty();
    }
    public void setHotelGroupID(Integer hotel_group_id) throws UoWException {
        this.hotel_group_id = hotel_group_id;
        markDirty();
    }

    public void remove() throws UoWException {
        markRemoved();
    }

    private void initMaps() {
    }
}

