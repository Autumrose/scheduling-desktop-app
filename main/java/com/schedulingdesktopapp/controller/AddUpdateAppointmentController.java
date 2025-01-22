package com.schedulingdesktopapp.controller;

import com.schedulingdesktopapp.dao.AppointmentsQuery;
import com.schedulingdesktopapp.dao.ContactsQuery;
import com.schedulingdesktopapp.dao.CustomersQuery;
import com.schedulingdesktopapp.dao.UsersQuery;
import com.schedulingdesktopapp.helper.GlobalHelperMethods;
import com.schedulingdesktopapp.model.Appointments;
import com.schedulingdesktopapp.model.UserLogIns;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller class for the AddUpdateAppointment.fxml view
 */
public class AddUpdateAppointmentController implements Initializable {
    private final ZoneId localId = ZoneId.systemDefault();
    Stage stage;
    Parent scene;
    @FXML
    private TextField appointmentID, title, description, location, type, createdOn, createdBy, updatedOn, updatedBy, userID;
    @FXML
    private DatePicker startDate, endDate;
    @FXML
    private ComboBox<LocalTime> startTime, endTime;
    @FXML
    private ComboBox<String> customer;
    @FXML
    private ChoiceBox<String> contact;
    private Boolean newAppointment;

    /**
     * Method to save the entered appointment information
     * If the boolean newAppointment is true then we use the sql INSERT command, else use sql UPDATE
     * @param event event for the action of clicking the save button
     * @throws IOException  May throw an IO except because of the change view
     * @throws SQLException May throw an sql exception because of the database query
     */
    @FXML
    void onActionClickSave(ActionEvent event) throws IOException, SQLException {
        // Query the contact ID using the contact selection
        int contact_ID = ContactsQuery.selectID(contact.getValue());

        // Query all of the selected customer's appointment
        ResultSet customerAppts = AppointmentsQuery.selectForCustomer(Integer.parseInt(customer.getValue().substring(0, customer.getValue().indexOf(" "))));

        // Convert the given start date/time and end date/time to UTC to send back to the database
        java.sql.Timestamp sqlStart = GlobalHelperMethods.convertToUTC(startDate.getValue(), startTime.getValue());
        java.sql.Timestamp sqlEnd = GlobalHelperMethods.convertToUTC(endDate.getValue(), endTime.getValue());

        // Iterate through the result set of all of the customer's appointments to ensure none are overlapping
        while (customerAppts.next()) {
            // Only compare if it has a different appointment ID than current appointment
            if (!newAppointment && customerAppts.getInt("Appointment_ID") != Integer.parseInt(appointmentID.getText())) {
                // Show error alert if the start or end time is between another appointment's time
                if (sqlStart.after(customerAppts.getTimestamp("Start")) && sqlStart.before(customerAppts.getTimestamp("End"))) {
                    GlobalHelperMethods.showErrorAlert("Overlapping Appointments", "You have an appointment at this time already, please select a new date/time");
                    return;
                } else if (sqlEnd.after(customerAppts.getTimestamp("Start")) && sqlEnd.before(customerAppts.getTimestamp("End"))) {
                    GlobalHelperMethods.showErrorAlert("Overlapping Appointments", "You have an appointment at this time already, please select a new date/time");
                    return;
                } else if (sqlStart.equals(customerAppts.getTimestamp("Start"))) {
                    GlobalHelperMethods.showErrorAlert("Overlapping Appointments", "You have an appointment at this time already, please select a new date/time");
                    return;
                }
            }
        }

        // Find the time now and who is updating the record for the created/updated by and on values
        LocalDateTime localNow = LocalDateTime.now();
        java.sql.Timestamp now = GlobalHelperMethods.convertToUTC(localNow.toLocalDate(), localNow.toLocalTime());
        String userName = UsersQuery.selectName(UserLogIns.userID);

        // If it's a new appointment use the INSERT sql command and create a new record
        if (newAppointment) {
            AppointmentsQuery.insert(String.valueOf(title.getText()), String.valueOf(description.getText()),
                    String.valueOf(location.getText()), String.valueOf(type.getText()), sqlStart, sqlEnd,
                    now, userName, now, userName, Integer.parseInt(customer.getValue().substring(0, customer.getValue().indexOf(" "))),
                    Integer.parseInt(userID.getText()), contact_ID);
        // If it's an existing appointment use the UPDATE sql command to just update the record that already exists
        } else {
            AppointmentsQuery.update(String.valueOf(title.getText()), String.valueOf(description.getText()),
                    String.valueOf(location.getText()), String.valueOf(type.getText()), sqlStart, sqlEnd,
                    Integer.parseInt(customer.getValue().substring(0, customer.getValue().indexOf(" "))),
                    UserLogIns.userID, contact_ID, now, userName, Integer.parseInt(appointmentID.getText()));
        }

        // Once the record is updated or created then return to the appointment view to see all records
        changeView(event, "/com/schedulingdesktopapp/schedulingdesktopapp/Appointments.fxml");
    }

    /**
     * Once the start time is set, update the end time values to be after the start time
     * @param event event for the action of selecting the start time
     * @throws IOException  May throw an IO except because of the change view
     */
    @FXML
    void onActionStartTimeSet(ActionEvent event) throws IOException {
        // Initialize time variables and list to store times
        ObservableList<LocalTime> timeOptions = FXCollections.observableArrayList();
        ZoneId etId = ZoneId.of("US/Eastern");
        ZonedDateTime localZone = ZonedDateTime.of(LocalDateTime.of(LocalDate.now(), LocalTime.of(22, 0)), localId);
        LocalTime end = ZonedDateTime.ofInstant(localZone.toInstant(), etId).toLocalTime();
        LocalTime start = startTime.getValue().plusMinutes(1);

        // Iterate from start time to end time and add a time option for every 1 minute
        while (!start.equals(end)) {
            timeOptions.add(start);
            start = start.plusMinutes(1);
        }
        timeOptions.add(end);

        // Update the field end time with the list of time options
        endTime.setItems(timeOptions);
    }

    /**
     * If user clicks cancel, confirm they would like to leave the page then return to the appointments screen
     * @param event event for the action of clicking the cancel button
     * @throws IOException  May throw an IO except because of the change view
     */
    @FXML
    void onActionClickCancel(ActionEvent event) throws IOException {
        // Use global helper to send the confirmation alert
        Optional<ButtonType> result = GlobalHelperMethods.showConfirmationAlert("Cancel Creating Appointment", "Are you sure you would like to exit the appointment? All information will be lost");

        // If the user answers yes then return to the appointments page
        if (result.isPresent() && result.get() == ButtonType.OK) {
            changeView(event, "/com/schedulingdesktopapp/schedulingdesktopapp/Appointments.fxml");
        }
    }



    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) throws RuntimeException {
        //stage.setTitle("Add Appointment");

        // Initialize boolean variable to signify if we should update or create new record
        newAppointment = true;

        // Set the contact list in the choicebox
        ObservableList<String> contacts = FXCollections.observableArrayList();
        try {
            ResultSet rs = ContactsQuery.select();
            while (rs.next()) {
                contacts.add(rs.getString("Contact_Name"));
            }
            contact.setItems(contacts);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Set the options for customers based on the current customers in the database
        ObservableList<String> customers = FXCollections.observableArrayList();
        try {
            ResultSet rs = CustomersQuery.select();
            while (rs.next()) {
                customers.add(rs.getInt("Customer_ID") + " " + rs.getString("Customer_Name"));
            }
            customer.setItems(customers);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Set the choicebox start and end time options
        ObservableList<LocalTime> timeOptions = FXCollections.observableArrayList();
        ZoneId etId = ZoneId.of("US/Eastern");

        ZonedDateTime localZone = ZonedDateTime.of(LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 0)), localId);
        LocalTime start = ZonedDateTime.ofInstant(localZone.toInstant(), etId).toLocalTime();

        localZone = ZonedDateTime.of(LocalDateTime.of(LocalDate.now(), LocalTime.of(22, 0)), localId);
        LocalTime end = ZonedDateTime.ofInstant(localZone.toInstant(), etId).toLocalTime();
        while (!start.equals(end)) {
            timeOptions.add(start);
            start = start.plusMinutes(1);
        }
        timeOptions.add(end);
        startTime.setItems(timeOptions);
        endTime.setItems(timeOptions);

        // Set the user ID to whoever is signed in
        userID.setText(String.valueOf(UserLogIns.userID));
    }

    /**
     * Helper method to change the view to a different page
     * @param event event for the action of clicking the save button
     * @param resource String that represents the path to the FXML file you would like to change views to
     * @throws IOException  May throw an IO except because of the change view
     */
    public void changeView(ActionEvent event, String resource) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(resource));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Helper method to set the appointment information from AppointmentController on an update
     * @param row input Appointment object row that contains appointment information to populate the fields
     * @throws SQLException May throw an sql exception because of the database query
     */
    public void sendAppointmentInfo(Appointments row) throws SQLException {
        newAppointment = false;
        appointmentID.setText(String.valueOf(row.getApptID()));
        title.setText(row.getTitle());
        description.setText(row.getDescription());
        location.setText(row.getLocation());
        type.setText(row.getType());
        startDate.setValue(row.getStartDate());
        startTime.setValue(row.getStartTime());
        endDate.setValue(row.getEndDate());
        endTime.setValue(row.getEndTime());
        createdOn.setText(String.valueOf(row.getCreatedDate()));
        createdBy.setText(String.valueOf(row.getCreatedBy()));
        updatedOn.setText(String.valueOf(row.getUpdatedLast()));
        updatedBy.setText(String.valueOf(row.getUpdatedBy()));
        customer.setValue(row.getCustomerID() + " " + CustomersQuery.selectName(row.getCustomerID()));
        userID.setText(String.valueOf(row.getUserID()));
        contact.setValue(row.getContactName());
    }

}
