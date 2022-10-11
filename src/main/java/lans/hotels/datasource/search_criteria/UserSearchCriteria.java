package lans.hotels.datasource.search_criteria;

import lans.hotels.domain.user.Role;

public class UserSearchCriteria extends AbstractSearchCriteria {
    private Integer userId;
    private String name;
    private String email;
    private Role role;
    private Integer hotelier_hotel_group_id;

    public UserSearchCriteria() {
    }

    public Integer getId() {
        return userId;
    }

    public void setId(Integer userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Integer getHotelierHotelGroupID() {
        return hotelier_hotel_group_id;
    }

    public void setHotelierHotelGroupID(Integer hotelier_hotel_group_id) {
        this.hotelier_hotel_group_id = hotelier_hotel_group_id;
    }

}
