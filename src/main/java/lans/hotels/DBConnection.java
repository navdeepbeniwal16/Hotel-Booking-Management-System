package lans.hotels;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String localurl = "jdbc:postgresql://localhost:5432/lans_hotels";
    private static final String localuser = "postgres";
    private static final String localpassword = "postgres";



    public static Connection connection()  {
        Connection conn = null;

        String stage = System.getenv("STAGE");
        String url = "", username = "", password = "";

        if (stage.equals(Stage.DEVELOPMENT.toString())) {
            url = System.getenv("DB_URL");
            username = System.getenv("DB_USERNAME");
            password = System.getenv("DB_PASSWORD");
            System.out.println(stage + " mode - using credentials:");
            System.out.println(username + "@" + password);
            System.out.println(url);
        } else if (stage.equals(Stage.PRODUCTION.toString())) {
            try {
                URI dbUri = new URI(System.getenv("DATABASE_URL"));
                username = dbUri.getUserInfo().split(":")[0];
                password = dbUri.getUserInfo().split(":")[1];
                url = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
            } catch (URISyntaxException e) {
                System.out.println(e);
                System.out.println("FATAL ERROR: invalid environment variable STAGE=" + stage);
                System.exit(1);
            }
        } else {
            System.out.println("FATAL ERROR: invalid environment variable STAGE=" + stage);
            System.exit(1);
        }



        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            //do something
            System.out.println(e);
        }
        return conn;
    }
}
