package lans.hotels.datasource.search_criteria;

public class BookingsSearchCriteria extends AbstractSearchCriteria {
    private Integer customerId;
    private Integer hotelGroupID;

    public BookingsSearchCriteria() {
    }

    public Integer getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getHotelGroupId() {
        return this.hotelGroupID;
    }

    public void setHotelGroupId(Integer hotelGroupID) {
        this.hotelGroupID = hotelGroupID;
    }

    @Override
    public String toString() {
        return "BookingsSearchCriteria{" +
                "customerId=" + customerId +
                '}';
    }
}
