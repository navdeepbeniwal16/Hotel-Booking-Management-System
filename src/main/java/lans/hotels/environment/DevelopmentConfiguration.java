package lans.hotels.environment;

import lans.hotels.datasource.DBConfiguration;

public class DevelopmentConfiguration extends DBConfiguration {
    public DevelopmentConfiguration(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }
}
