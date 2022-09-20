package lans.hotels.datasource.search_criteria;

import java.util.Date;

public class HotelGroupSearchCriteria extends AbstractSearchCriteria {
    Integer hotelGroupID;

    public HotelGroupSearchCriteria() {
    }
    public Integer getHotelGroupID() { return this.hotelGroupID; }
    public void setHotelGroupID(Integer id) { this.hotelGroupID = id; }

}
