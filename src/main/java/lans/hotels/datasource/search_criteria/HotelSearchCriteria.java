package lans.hotels.datasource.search_criteria;
import lans.hotels.domain.utils.DateRange;

public class HotelSearchCriteria extends AbstractSearchCriteria {
    Integer id;
    String city;
    Integer hotel_group_id;

    public HotelSearchCriteria() {
    }

    public Integer getId() { return this.id; }

    public void setId(Integer id) { this.id = id; }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getHotelGroupId() {
        return hotel_group_id;
    }

    public void setHotelGroupId(Integer hotelGroupId) {
        this.hotel_group_id = hotelGroupId;
    }
}
