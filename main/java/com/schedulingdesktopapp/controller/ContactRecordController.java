package com.schedulingdesktopapp.controller;

import com.schedulingdesktopapp.dao.AppointmentsQuery;
import com.schedulingdesktopapp.dao.ContactsQuery;
import com.schedulingdesktopapp.model.Appointments;
import com.schedulingdesktopapp.model.Contacts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class ContactRecordController implements Initializable {
    Stage stage;
    Parent scene;
    @FXML
    private TextField id, name, email;
    @FXML
    private TableColumn<Appointments, Integer> idCol, customerIDCol;
    @FXML
    private TableColumn<Appointments, String> titleCol, descCol;
    @FXML
    private TableColumn<Appointments, LocalDate> startDateCol, endDateCol;
    @FXML
    private TableColumn<Appointments, LocalTime> startTimeCol, endTimeCol;
    @FXML
    private TableView<Appointments> tableView;

    /**
     * Change view back to the contacts list view
     * @param event event passed in when return button is clicked
     * @throws IOException  May throw an IO except because of the view change
     */
    @FXML
    public void onActionClickReturnToContacts(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/com/schedulingdesktopapp/schedulingdesktopapp/Contacts.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * Convert the time in UTC to local time to display
     * @param dateTime date time in UTC
     * @return ZonedDateTime value that's converted
     */
    private ZonedDateTime convertedTime(java.sql.Timestamp dateTime) {
        ZoneId utcId = ZoneId.of("UTC");
        ZoneId localId = ZoneId.systemDefault();

        ZonedDateTime utcZone = ZonedDateTime.of(dateTime.toLocalDateTime(), utcId);
        return ZonedDateTime.ofInstant(utcZone.toInstant(), localId);

    }

    /**
     * Prepopulate the fields with the selected contact information from the selected table in Contacts view
     * @param contact the selected row that contains all of the values
     * @throws SQLException May throw an sql exception because of the database query
     */
    public void sendContactInfo(Contacts contact) throws SQLException {
        // Set the text fields with the contact information
        id.setText(String.valueOf(contact.getId()));
        name.setText(String.valueOf(contact.getName()));
        email.setText(String.valueOf(contact.getEmail()));

        ObservableList<Appointments> allData = FXCollections.observableArrayList();

        try {
            // Select all appointments this contact has scheduled with them
            ResultSet weekRS = AppointmentsQuery.selectForContact(contact.getId());
            while (weekRS.next()) {
                // Convert the timestampt to local date time then use the time zone to get the zoned time
                ZonedDateTime start = convertedTime(weekRS.getTimestamp("Start"));
                ZonedDateTime end = convertedTime(weekRS.getTimestamp("End"));

                // Create a new Appointment object with the result set current row information
                Appointments appt = new Appointments(weekRS.getInt("Appointment_ID"), weekRS.getString("Title"), weekRS.getString("Description"), weekRS.getString("Location"), weekRS.getString("Type"), start.toLocalDate(), start.toLocalTime(), end.toLocalDate(), end.toLocalTime(), weekRS.getDate("Create_Date"), weekRS.getString("Created_By"), weekRS.getTimestamp("Last_Update"), weekRS.getString("Last_Updated_By"), weekRS.getInt("Customer_ID"), weekRS.getInt("User_ID"), weekRS.getInt("Contact_ID"), ContactsQuery.selectName(weekRS.getInt("Contact_ID")));
                allData.add(appt);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Set your table columns
        tableView.setItems(allData);
        idCol.setCellValueFactory(new PropertyValueFactory<>("apptID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
    }
}
