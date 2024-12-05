package com.example.proj.Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/shibalib";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";


    /**
     * Retrieves a shared database connection instance. If no connection exists or the connection is closed,
     * a new connection is created. This method ensures thread-safety using double-checked locking.
     *
     * @return a valid {@link Connection} instance.
     * @throws SQLException if a database access error occurs or the URL is invalid.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
    }


    /**
     * Closes the current database connection if it exists and is open. Sets the connection to null after closing.
     * This method ensures safe closure of resources.
     */
    public static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
