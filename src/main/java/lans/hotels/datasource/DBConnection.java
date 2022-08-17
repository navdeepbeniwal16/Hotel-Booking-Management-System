package lans.hotels.datasource;

import lans.hotels.environment.DBConfiguration;

import java.sql.Connection;

public abstract class DBConnection {
    protected final DBConfiguration config;
    public DBConnection(DBConfiguration config) {
        this.config = config;
    }

    public abstract Connection connection();
}