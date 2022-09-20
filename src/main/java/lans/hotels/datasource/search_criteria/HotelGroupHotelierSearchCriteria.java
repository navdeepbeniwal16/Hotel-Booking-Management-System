package lans.hotels.datasource.search_criteria;

import java.util.Date;

public class HotelGroupHotelierSearchCriteria extends AbstractSearchCriteria {
    Integer hotelGroupID;

    public HotelGroupHotelierSearchCriteria() {
    }
    public Integer getHotelGroupID() { return this.hotelGroupID; }
    public void setHotelGroupID(Integer id) { this.hotelGroupID = id; }

}
