package lans.hotels.datasource.search_criteria;

public class CustomerSearchCriteria extends AbstractSearchCriteria {
    private Integer customerId;

    public CustomerSearchCriteria() {
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
}
