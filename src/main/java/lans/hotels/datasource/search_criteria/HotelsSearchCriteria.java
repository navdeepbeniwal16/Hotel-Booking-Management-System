package lans.hotels.datasource.search_criteria;

import java.util.Date;

public class HotelsSearchCriteria extends AbstractSearchCriteria {
    String location;
    Integer hotelGroupId;
    Date startDate;
    Date endDate;

    Integer hotelId;

    public HotelsSearchCriteria() {
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getHotelGroupId() {
        return hotelGroupId;
    }

    public void setHotelGroupId(Integer hotelGroupId) {
        this.hotelGroupId = hotelGroupId;
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

    public Integer getHotelId() { return this.hotelId; }
    public void setHotelId(Integer id) { this.hotelId = id; }

}
