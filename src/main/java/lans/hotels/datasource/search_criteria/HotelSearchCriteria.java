package lans.hotels.datasource.search_criteria;
import lans.hotels.domain.utils.DateRange;

public class HotelSearchCriteria extends AbstractSearchCriteria {
    Integer id;
    String city;
    Integer hotel_group_id;
    Boolean is_active;
    String name;
    String email;

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

    public Boolean getIsActive() { return this.is_active; }

    public void setIsActive(Boolean is_active) { this.is_active = is_active; }

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return this.email; }

    public void setEmail(String email) { this.email = email; }

}
