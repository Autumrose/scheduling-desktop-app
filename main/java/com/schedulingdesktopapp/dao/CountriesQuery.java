package com.schedulingdesktopapp.dao;

import com.schedulingdesktopapp.helper.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountriesQuery {

    /**
     * Method to select all of the records in the country table
     * @return the ResultSet of all the records
     * @throws SQLException May throw an sql exception because of the database query
     */
    public static ResultSet select() throws SQLException {
        String sql = "SELECT * FROM client_schedule.countries";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        return ps.executeQuery();
    }

    /**
     * Method to find the name of the country with the given id
     * @param countryID indicates what country to look for
     * @return the country name
     * @throws SQLException May throw an sql exception because of the database query
     */
    public static String selectCountryName(int countryID) throws SQLException {
        String sql = "SELECT * FROM client_schedule.countries WHERE Country_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, countryID);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getString("Country");
    }

    /**
     * Method to find the id of the country with the given name
     * @param countryName indicates what country to look for
     * @return the country id
     * @throws SQLException May throw an sql exception because of the database query
     */
    public static int selectCountryID(String countryName) throws SQLException {
        String sql = "SELECT * FROM client_schedule.countries WHERE Country = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, countryName);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt("Country_ID");
    }
}
