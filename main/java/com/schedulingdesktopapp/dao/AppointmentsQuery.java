package com.schedulingdesktopapp.dao;

import com.schedulingdesktopapp.helper.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class containing all of the methods for any queries to the appointment table within the database
 */
public class AppointmentsQuery {

    /**
     * Method to select all records from the database
     * @return a resultset with all of the records
     * @throws SQLException May throw an sql exception because of the database query
     */
    public static ResultSet select() throws SQLException {
        String sql = "SELECT * FROM client_schedule.APPOINTMENTS";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        return ps.executeQuery();
    }

    /**
     * Method to select all records from the database for a specific customer
     * @param customer_ID what customer to lookup
     * @return a resultset with all of the records for a specific customer
     * @throws SQLException May throw an sql exception because of the database query
     */
    public static ResultSet selectForCustomer(int customer_ID) throws SQLException {
        String sql = "SELECT * FROM client_schedule.APPOINTMENTS WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customer_ID);
        return ps.executeQuery();
    }

    /**
     * Method to select all records from the database for a specific contact
     * @param contactID what customer to lookup
     * @return a resultset with all of the records for a specific contact
     * @throws SQLException May throw an sql exception because of the database query
     */
    public static ResultSet selectForContact(int contactID) throws SQLException {
        String sql = "SELECT * FROM client_schedule.APPOINTMENTS WHERE Contact_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, contactID);
        return ps.executeQuery();
    }

    /**
     * Method to select all records from the database within the given timeframe
     * @param startDate start date of the time frame
     * @param endDate end  date of the time frame
     * @return the ResultSet of all records within the given timeframe
     * @throws SQLException May throw an sql exception because of the database query
     */
    public static ResultSet selectTimeFrame(java.sql.Timestamp startDate, java.sql.Timestamp endDate) throws SQLException {
        String sql = "SELECT * FROM client_schedule.APPOINTMENTS WHERE Start >= ? AND End <= ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setTimestamp(1, startDate);
        ps.setTimestamp(2, endDate);
        return ps.executeQuery();
    }

    /**
     * Select all of the distinct (non repeats) values from the type column
     * @return the ResultSet containing all of the different types listed in the type column
     * @throws SQLException May throw an sql exception because of the database query
     */
    public static ResultSet selectTypeValues() throws SQLException {
        String sql = "SELECT DISTINCT Type from client_schedule.appointments";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        return ps.executeQuery();
    }

    /**
     * Select all of the appointments with the specified type
     * @param type the type to filter the appointments with
     * @return a ResultSet of all the appointments that have the given type
     * @throws SQLException May throw an sql exception because of the database query
     */
    public static ResultSet selectAllAppointmentsType(String type) throws SQLException {
        String sql = "SELECT * from client_schedule.appointments WHERE Type = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, type);
        return ps.executeQuery();
    }

    /**
     * Select all of the appointments sorted in ascending order by the Start column
     * @return Sorted ResultSet
     * @throws SQLException May throw an sql exception because of the database query
     */
    public static ResultSet selectSorted() throws SQLException {
        String sql = "SELECT * FROM client_schedule.APPOINTMENTS ORDER BY Start ASC";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        return ps.executeQuery();
    }

    /**
     * Use all of the input values to insert a new record into the database
     * @param title, description, location, type, start, end, createdOn, createdBy, updatedOn, updatedBy, customerID, userID, contactID
     *                  input values for the insert call to the database for an appointment
     * @throws SQLException May throw an sql exception because of the database query
     */
    public static void insert(String title, String description, String location, String type, java.sql.Timestamp start,
                             java.sql.Timestamp end, java.sql.Timestamp createdOn, String createdBy,
                             java.sql.Timestamp updatedOn, String updatedBy, int customerID, int userID,
                             int contactID) throws SQLException {
        String sql = "INSERT INTO client_schedule.appointments (Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, end);
        ps.setTimestamp(7, createdOn);
        ps.setString(8, createdBy);
        ps.setTimestamp(9, updatedOn);
        ps.setString(10, updatedBy);
        ps.setInt(11, customerID);
        ps.setInt(12, userID);
        ps.setInt(13, contactID);

        ps.executeUpdate();
    }

    /**
     * Update a record that already exists using the given input information
     * @param title, description, location, type, start, end, customerID, userID, contactID, updatedOn, updatedBy, appointmentID,
     *                  input values for the update call to the database for an appointment
     * @throws SQLException May throw an sql exception because of the database query
     */
    public static void update(String title, String description, String location, String type, java.sql.Timestamp start, java.sql.Timestamp end, int customerID, int userID, int contactID, java.sql.Timestamp updatedOn, String updatedBy, int appointmentID) throws SQLException {
        String sql = "UPDATE client_schedule.appointments SET Title = ?, Description = ?, Location =?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ?, Last_Update = ?, Last_Updated_By = ? WHERE Appointment_ID = ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, end);
        ps.setInt(7, customerID);
        ps.setInt(8, userID);
        ps.setInt(9, contactID);
        ps.setTimestamp(10, updatedOn);
        ps.setString(11, updatedBy);
        ps.setInt(12, appointmentID);

        ps.executeUpdate();
    }

    /**
     * Delete all of the appointments connected to the input id
     * @param id customer's id used to filter
     * @throws SQLException May throw an sql exception because of the database query
     */
    public static void deleteCustomerAppointments(int id) throws SQLException {
        String sql = "DELETE FROM client_schedule.APPOINTMENTS WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    /**
     * Delete the specific record with the given apptID (primary key)
     * @param apptID indicator of what record to delete
     * @throws SQLException May throw an sql exception because of the database query
     */
    public static void delete(int apptID) throws SQLException {
        String sql = "DELETE FROM client_schedule.APPOINTMENTS WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, apptID);
        ps.executeUpdate();
    }
}
