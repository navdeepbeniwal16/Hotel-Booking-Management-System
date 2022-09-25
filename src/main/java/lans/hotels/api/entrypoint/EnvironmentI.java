package lans.hotels.api.entrypoint;

import lans.hotels.datasource.DBConfiguration;

public interface EnvironmentI {
    DBConfiguration getDBConfiguration();
}
