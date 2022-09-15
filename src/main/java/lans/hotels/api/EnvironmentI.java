package lans.hotels.api;

import lans.hotels.datasource.DBConfiguration;

public interface EnvironmentI {
    DBConfiguration getDBConfiguration();
}
