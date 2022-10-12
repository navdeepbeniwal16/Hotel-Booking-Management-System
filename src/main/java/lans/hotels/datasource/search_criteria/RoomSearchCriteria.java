package lans.hotels.datasource.search_criteria;

public class RoomSearchCriteria extends AbstractSearchCriteria{
    Integer hotelId;
    Boolean isAvailable;
    Integer number;

    public RoomSearchCriteria() {
    }

    public Integer getRoomNumber() {
        return number;
    }

    public void setRoomNumber(Integer number) {
        this.number = number;
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
