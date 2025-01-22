package com.schedulingdesktopapp.dao;

import com.schedulingdesktopapp.helper.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class containing all of the methods for any queries to the first_level_divisions table within the database
 */
public class DivisionsQuery {

    /**
     * Method to select all records from the database
     * @return a resultset with all of the records
     * @throws SQLException May throw an sql exception because of the database query
     */
    public static ResultSet select() throws SQLException {
        String sql = "SELECT * FROM client_schedule.first_level_divisions";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        return ps.executeQuery();
    }

    /**
     * Method to find the name of the division with the given id
     * @param divisionID indicates what division to look for
     * @return the division name
     * @throws SQLException May throw an sql exception because of the database query
     */
    public static String selectDivisionName(int divisionID) throws SQLException {
        String sql = "SELECT * FROM client_schedule.first_level_divisions WHERE Division_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, divisionID);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getString("Division");
    }

    /**
     * Method to find the id of the country with the given division id
     * @param divisionID indicates what country to look for
     * @return the country id
     * @throws SQLException May throw an sql exception because of the database query
     */
    public static int selectCountryID(int divisionID) throws SQLException {
        String sql = "SELECT * FROM client_schedule.first_level_divisions WHERE Division_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, divisionID);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt("Country_ID");
    }

    /**
     * Method to find the id of the division with the given division name
     * @param divisionName indicates what division to look for
     * @return the country id
     * @throws SQLException May throw an sql exception because of the database query
     */
    public static int selectDivisionID(String divisionName) throws SQLException {
        String sql = "SELECT * FROM client_schedule.first_level_divisions WHERE Division = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, divisionName);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt("Division_ID");
    }

    /**
     * Select all of the divisions that have a given country ID
     * @param countryID given id number for what records we would like
     * @return the ResultSet containing all of the divisions in the givenc ountry
     * @throws SQLException May throw an sql exception because of the database query
     */
    public static ResultSet selectForCountry(int countryID) throws SQLException {
        String sql = "SELECT * FROM client_schedule.first_level_divisions WHERE Country_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, countryID);
        return ps.executeQuery();
    }
}
