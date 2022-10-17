package lans.hotels.datasource.connections;

        import lans.hotels.datasource.DBConfiguration;

        import java.sql.Connection;
        import java.sql.DriverManager;
        import java.sql.SQLException;

public class PostgresConnection extends DBConnection {
    public PostgresConnection(DBConfiguration config) {
        super(config);
    }

    public Connection getConnection()  {
        Connection conn = null;
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            conn = DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return conn;
    }
}
