package lans.hotels.datasource.search_criteria;

public class UserSearchCriteria extends AbstractSearchCriteria {
    private Integer userId;
    private String name;
    private String email;
    private Integer role;

    public UserSearchCriteria() {
    }

    public Integer getId() {
        return userId;
    }

    public void setId(Integer userId) {
        this.userId = userId;
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

    public Integer getRole() {
        return this.role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }


}
