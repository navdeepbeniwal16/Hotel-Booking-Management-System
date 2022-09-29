package lans.hotels.datasource.search_criteria;

public class RoomSearchCriteria extends AbstractSearchCriteria{
    Integer hotelId;
    Boolean isAvailable;

    public RoomSearchCriteria() {
    }

    public Integer getHotelId() {
        return hotelId;
    }

    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }
}
