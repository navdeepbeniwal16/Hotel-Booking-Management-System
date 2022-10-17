package lans.hotels.datasource.search_criteria;

public class RoomSearchCriteria extends AbstractSearchCriteria{
    Integer hotelId;
    Boolean isAvailable;
    Integer number;
    Integer roomID;

    public RoomSearchCriteria() {
    }

    public Integer getRoomID() {
        return this.roomID;
    }

    public void setRoomID(Integer roomID) {
        this.roomID = roomID;
    }

    public Integer getRoomNumber() {
        return number;
    }

    public void setRoomNumber(Integer number) {
        this.number = number;
    }

    public Integer getHotelId() {
        return this.hotelId;
    }

    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
    }

    public Boolean getAvailable() {
        return this.isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }
}
