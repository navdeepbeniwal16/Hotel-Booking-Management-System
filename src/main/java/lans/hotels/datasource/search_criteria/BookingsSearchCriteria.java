package lans.hotels.datasource.search_criteria;

import java.util.Date;

public class BookingsSearchCriteria extends AbstractSearchCriteria {
    private Integer bookingId;
    private Integer customerId;
    private String customerEmail;
    private Integer hotelGroupID;
    private Integer hotelId;
    private Date startDate;
    private Date endDate;

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

    public Integer getHotelId() {
        return hotelId;
    }

    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
    }

    public Integer getHotelGroupID() {
        return hotelGroupID;
    }

    public void setHotelGroupID(Integer hotelGroupID) {
        this.hotelGroupID = hotelGroupID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "BookingsSearchCriteria{" +
                "bookingId=" + bookingId +
                ", customerId=" + customerId +
                ", customerEmail='" + customerEmail + '\'' +
                ", hotelGroupID=" + hotelGroupID +
                ", hotelId=" + hotelId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
