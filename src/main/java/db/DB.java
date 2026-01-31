package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    private static DB instance;
    private Connection connection;

    private DB() throws SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/musicstore", "postgres", "1234");
    }

    public static DB getInstance() throws SQLException {
        if (instance == null || instance.connection.isClosed()) {
            instance = new DB();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public static Connection connect() throws SQLException {
        return getInstance().getConnection();
    }
}