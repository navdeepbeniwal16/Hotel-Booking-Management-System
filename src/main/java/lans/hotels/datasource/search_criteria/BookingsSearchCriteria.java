package lans.hotels.datasource.search_criteria;

public class BookingsSearchCriteria extends AbstractSearchCriteria {
    private Integer customerId;

    public BookingsSearchCriteria() {
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "BookingsSearchCriteria{" +
                "customerId=" + customerId +
                '}';
    }
}
