package lans.hotels.datasource.search_criteria;

public class UserSearchCriteria extends AbstractSearchCriteria {
    private Integer userId;
    private String name;
    private Boolean isActive;
    private String email;

    public UserSearchCriteria() {
    }

    public Integer getId() {
        return userId;
    }

    public void setId(Integer userId) {
        this.userId = userId;
    }




}
