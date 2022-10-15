package lans.hotels.datasource.search_criteria;

import lans.hotels.domain.utils.DateRange;

public class RoomBookingSearchCriteria extends AbstractSearchCriteria {
    private Integer bookingId;
    Integer room_id;
    DateRange date_range;

    public RoomBookingSearchCriteria() {
    }

    public Integer getBookingId() {
        return this.bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getRoomID() {
        return this.room_id;
    }

    public void setRoomID(Integer room_id) {
        this.room_id = room_id;
    }

    public DateRange getDate_range() {
        return this.date_range;
    }

    public void setDate_range(DateRange date_range) {
        this.date_range = date_range;
    }
}
