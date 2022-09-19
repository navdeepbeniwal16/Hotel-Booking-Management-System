package lans.hotels.datasource.search_criteria;

public class HotelsSearchCriteria extends AbstractSearchCriteria {
    String location;

    public HotelsSearchCriteria() {
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
