package com.schedulingdesktopapp.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class to open and close your connection with the database
 */
public abstract class JDBC {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String dbName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + dbName + "?connectionTimeZone = SERVER";
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String userName = "sqlUser";
    private static final String password = "Passw0rd!";
    public static Connection connection;

    /**
     * Method to start your connection with the database
     */
    public static void openConnection() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        // If successful, return the Connection object
        connection = DriverManager.getConnection(jdbcUrl, userName, password);
    }

    /**
     * Method to close your connection once complete
     */
    public static void closeConnection() throws SQLException {
        connection.close();
    }
}
