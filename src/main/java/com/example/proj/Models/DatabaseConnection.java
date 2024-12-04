package com.example.proj.Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/shibalib";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";

    private static Connection connection;

    /**
     * Retrieves a shared database connection instance. If no connection exists or the connection is closed,
     * a new connection is created. This method ensures thread-safety using double-checked locking.
     *
     * @return a valid {@link Connection} instance.
     * @throws SQLException if a database access error occurs or the URL is invalid.
     */
    public static Connection getConnection() throws SQLException {

        // double-check the connection.
        if (connection == null || connection.isClosed()) {

            // synchronized ensure checking and connecting just can be executed by 1 thread.
            // if too much thread, the connection can be broken.
            synchronized (DatabaseConnection.class) {
                if (connection == null || connection.isClosed()) {
                    connection = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
                }
            }
        }
        return connection;
    }

    /**
     * Closes the current database connection if it exists and is open. Sets the connection to null after closing.
     * This method ensures safe closure of resources.
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
