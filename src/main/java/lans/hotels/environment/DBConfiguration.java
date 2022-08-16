package lans.hotels.environment;

public abstract class DBConfiguration {
    protected String url;
    protected String username;
    protected String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }
}
