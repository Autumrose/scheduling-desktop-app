package com.schedulingdesktopapp.dao;

import com.schedulingdesktopapp.helper.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class containing all of the methods for any queries to the users table within the database
 */
public class UsersQuery {

    /**
     * Method to validate the input username and password and ensure they match each other
     * @param username input String containing the entered username
     * @param password input String containing the entered password
     * @return -3 if invalid, return the user id if it is valid
     * @throws SQLException May throw an sql exception because of the database query
     */
    public static int validateLogin(String username, String password) throws SQLException {
        String sql = "SELECT * FROM USERS WHERE User_Name = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, username);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            if (password.equals(rs.getString("Password"))) {
                return rs.getInt("User_ID");
            }
        }
        return -3;
    }

    /**
     * Method takes a user id and returns their name pulled from the database
     * @param id input user id
     * @return the username
     * @throws SQLException May throw an sql exception because of the database query
     */
    public static String selectName(int id) throws SQLException {
        String sql = "SELECT * FROM USERS WHERE User_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getString("User_Name");
    }

}
