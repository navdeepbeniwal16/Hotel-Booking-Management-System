package lans.hotels.datasource.search_criteria;

public class HotelierSearchCriteria extends AbstractSearchCriteria {
    private Integer hotelierId;
    private String name;
    private Boolean isActive;
    private String email;

    public HotelierSearchCriteria() {
    }

    public Integer getId() {
        return hotelierId;
    }

    public void setId(Integer hotelierId) {
        this.hotelierId = hotelierId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }


}
