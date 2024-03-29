package lans.hotels.datasource.search_criteria;

public class HotelGroupSearchCriteria extends AbstractSearchCriteria {
    private Integer hotelGroupID;
    private String name;

    public HotelGroupSearchCriteria() {
    }
    public Integer getHotelGroupID() { return this.hotelGroupID; }
    public void setHotelGroupID(Integer id) { this.hotelGroupID = id; }

    public String getHotelGroupName() {
        return this.name;
    }

    public void setHotelGroupName(String name) {
        this.name = name;
    }

}
