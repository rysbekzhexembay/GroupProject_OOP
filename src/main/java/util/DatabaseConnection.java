package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public static Connection getConnection() throws Exception {
        String url = "jdbc:postgresql://localhost:5432/musicstore";
        String user = "postgres";
        String password = "password";

        return DriverManager.getConnection(url, user, password);
    }
}