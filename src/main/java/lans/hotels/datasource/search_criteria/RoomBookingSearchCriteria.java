package lans.hotels.datasource.search_criteria;

public class RoomBookingSearchCriteria extends AbstractSearchCriteria {
    private Integer bookingId;

    public RoomBookingSearchCriteria() {
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }
}
