package com.schedulingdesktopapp.dao;

import com.schedulingdesktopapp.helper.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class containing all of the methods for any queries to the customers table within the database
 */
public abstract class CustomersQuery {

    /**
     * Method to select all records from the database
     * @return a resultset with all of the records
     * @throws SQLException May throw an sql exception because of the database query
     */
    public static ResultSet select() throws SQLException {
        String sql = "SELECT * FROM client_schedule.CUSTOMERS";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        return ps.executeQuery();
    }

    /**
     * Method to find the name of the customer with the given id
     * @param customerID indicates what customer to look for
     * @return the customer name
     * @throws SQLException May throw an sql exception because of the database query
     */
    public static String selectName(int customerID) throws SQLException {
        String sql = "SELECT * FROM client_schedule.CUSTOMERS WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerID);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getString("Customer_Name");
    }

    /**
     * Select all of the columns from the customer table joined with the division and country tables with the ids
     * @return Sorted ResultSet
     * @throws SQLException May throw an sql exception because of the database query
     */
    public static ResultSet selectSortByLocation() throws SQLException {
        String sql = "SELECT * FROM client_schedule.customers INNER JOIN client_schedule.first_level_divisions ON customers.Division_ID=first_level_divisions.Division_ID INNER JOIN client_schedule.countries ON first_level_divisions.Country_ID=countries.Country_ID";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        return ps.executeQuery();
    }

    /**
     * Use all of the input values to insert a new record into the database
     * @param name, address, postalCode, phone, createdOn, createdBy, updatedOn, updatedBy, Division_ID
     *                  input values for the insert call to the database for a customer
     * @throws SQLException May throw an sql exception because of the database query
     */
    public static void insert(String name, String address, String postalCode, String phone, java.sql.Timestamp createdOn, String createdBy,
                             java.sql.Timestamp updatedOn, String updatedBy, int Division_ID) throws SQLException {
        String sql = "INSERT INTO client_schedule.customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setTimestamp(5, createdOn);
        ps.setString(6, createdBy);
        ps.setTimestamp(7, updatedOn);
        ps.setString(8, updatedBy);
        ps.setInt(9, Division_ID);

        ps.executeUpdate();
    }

    /**
     * Update a record that already exists using the given input information
     * @param name, address, postalCode, phone, updatedOn, updatedBy, Division_ID
     *                  input values for the update call to the database for a customer
     * @throws SQLException May throw an sql exception because of the database query
     */
    public static void update(String name, String address, String postalCode, String phone,
                              int Division_ID, java.sql.Timestamp updatedOn, String updatedBy, int id) throws SQLException {
        String sql = "UPDATE client_schedule.customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, " +
                "Division_ID = ?, Last_Update = ?, Last_Updated_By = ?  WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setInt(5, Division_ID);
        ps.setTimestamp(6, updatedOn);
        ps.setString(7, updatedBy);
        ps.setInt(8, id);

        ps.executeUpdate();
    }

    /**
     * Delete the specific record with the given customer ID
     * @param customerID indicator of what record to delete
     * @throws SQLException May throw an sql exception because of the database query
     */
    public static int delete(int customerID) throws SQLException {
        String sql = "DELETE FROM client_schedule.CUSTOMERS WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerID);

        return ps.executeUpdate();
    }
}
