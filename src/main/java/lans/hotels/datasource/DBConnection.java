package lans.hotels.datasource;

import lans.hotels.environment.DBConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private final DBConfiguration config;
    public DBConnection(DBConfiguration config) {
        this.config = config;
    }

    public Connection connection()  {
        Connection conn = null;
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            conn = DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
        } catch (SQLException e) {
            System.out.println(e);
        }
        return conn;
    }
}
