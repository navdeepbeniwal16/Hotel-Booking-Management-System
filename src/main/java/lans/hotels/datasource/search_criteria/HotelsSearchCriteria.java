package lans.hotels.datasource.search_criteria;

public class HotelsSearchCriteria extends AbstractSearchCriteria {
    String location;
    Integer hotelGroupId;

    public HotelsSearchCriteria() {
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getHotelGroupId() {
        return hotelGroupId;
    }

    public void setHotelGroupId(Integer hotelGroupId) {
        this.hotelGroupId = hotelGroupId;
    }
}
