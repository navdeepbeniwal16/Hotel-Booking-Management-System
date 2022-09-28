package lans.hotels.datasource.search_criteria;

public class BookingsSearchCriteria extends AbstractSearchCriteria {
    private Integer bookingId;
    private Integer customerId;
    private String customerEmail;
    private Integer hotelGroupID;

    public BookingsSearchCriteria() {
    }

    public Integer getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerEmail() {
        return this.customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Integer getHotelGroupId() {
        return this.hotelGroupID;
    }

    public void setHotelGroupId(Integer hotelGroupID) {
        this.hotelGroupID = hotelGroupID;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    @Override
    public String toString() {
        return "BookingsSearchCriteria{" +
                "customerId=" + customerId +
                '}';
    }
}
