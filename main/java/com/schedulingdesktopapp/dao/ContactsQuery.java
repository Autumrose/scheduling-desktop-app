package com.schedulingdesktopapp.dao;

import com.schedulingdesktopapp.helper.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class containing all of the methods for any queries to the contacts table within the database
 */
public class ContactsQuery {

    /**
     * Method to select all records from the database
     * @return a resultset with all of the records
     * @throws SQLException May throw an sql exception because of the database query
     */
    public static ResultSet select() throws SQLException {
        String sql = "SELECT * FROM client_schedule.CONTACTS";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        return ps.executeQuery();
    }

    /**
     * Method to find the id of the contact with the given name
     * @param name indicates what contact to look for
     * @return the contact id
     * @throws SQLException May throw an sql exception because of the database query
     */
    public static int selectID(String name) throws SQLException {
        String sql = "SELECT * FROM client_schedule.CONTACTS WHERE Contact_Name = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, name);
        ResultSet id = ps.executeQuery();
        id.next();
        return id.getInt("Contact_ID");
    }

    /**
     * Method to find the name of the contact with the given id
     * @param id indicates what contact to look for
     * @return the contact name
     * @throws SQLException May throw an sql exception because of the database query
     */
    public static String selectName(int id) throws SQLException {
        String sql = "SELECT * FROM client_schedule.CONTACTS WHERE Contact_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet name = ps.executeQuery();
        name.next();
        return name.getString("Contact_Name");
    }
}
