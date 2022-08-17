package lans.hotels.environment;

import lans.hotels.datasource.DBConfiguration;

import java.net.URI;
import java.net.URISyntaxException;

public class HerokuConfiguration extends DBConfiguration {
    public HerokuConfiguration(String url) throws URISyntaxException {
        URI dbUri = new URI(url);
        this.username = dbUri.getUserInfo().split(":")[0];
        this.password = dbUri.getUserInfo().split(":")[1];
        this.url = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
    }
}
