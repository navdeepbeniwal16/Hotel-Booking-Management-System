package lans.hotels.datasource.search_criteria;

public class RoomSearchCriteria extends AbstractSearchCriteria {
    Integer hotel_id;

    public RoomSearchCriteria() {
    }

    public Integer getHotelId() {
        return hotel_id;
    }

    public void setHotelId(Integer hotel_id) {
        this.hotel_id = hotel_id;
    }
}
