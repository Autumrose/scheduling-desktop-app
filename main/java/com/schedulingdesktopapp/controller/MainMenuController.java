package com.schedulingdesktopapp.controller;

import com.schedulingdesktopapp.GeneralInterface;
import com.schedulingdesktopapp.dao.AppointmentsQuery;
import com.schedulingdesktopapp.helper.GlobalHelperMethods;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;


public class MainMenuController implements Initializable {
    Stage stage;
    Parent scene;

    /**
     * Option to navigate to the customers view, changes the view to the fxml file for the customers
     * @param event event passed in when customers button is clicked
     * @throws IOException  May throw an IO except because of the change view
     * @throws SQLException May throw an sql exception because of the database query
     */
    @FXML
    void onActionClickCustomers(ActionEvent event) throws IOException, SQLException {
        changeView(event, "/com/schedulingdesktopapp/schedulingdesktopapp/CustomerRecord.fxml");
    }

    /**
     * Option to navigate to the appointments view, changes the view to the fxml file for the appointments
     * @param event event passed in when appointments button is clicked
     * @throws IOException  May throw an IO except because of the change view
     * @throws SQLException May throw an sql exception because of the database query
     */
    @FXML
    void onActionClickAppointments(ActionEvent event) throws IOException, SQLException {
        changeView(event, "/com/schedulingdesktopapp/schedulingdesktopapp/Appointments.fxml");
    }

    /**
     * Option to navigate to the contacts view, changes the view to the fxml file for the contacts
     * @param event event passed in when contacts button is clicked
     * @throws IOException  May throw an IO except because of the change view
     * @throws SQLException May throw an sql exception because of the database query
     */
    @FXML
    void onActionClickContacts(ActionEvent event) throws IOException {
        changeView(event, "/com/schedulingdesktopapp/schedulingdesktopapp/Contacts.fxml");
    }

    /**
     * Option to navigate to the customer's report view, changes the view to the fxml file for the customers by location
     * @param event event passed in when customers location report button is clicked
     * @throws IOException  May throw an IO except because of the change view
     * @throws SQLException May throw an sql exception because of the database query
     */
    @FXML
    public void onActionClickCustomersLocationReport(ActionEvent event) throws IOException {
        changeView(event, "/com/schedulingdesktopapp/schedulingdesktopapp/CustomerReport.fxml");
    }

    /**
     * Option to navigate to the appointment's report view, changes the view to the fxml file for the appointments by type and month
     * @param event event passed in when appointments report button is clicked
     * @throws IOException  May throw an IO except because of the change view
     * @throws SQLException May throw an sql exception because of the database query
     */
    @FXML
    public void onActionClickAppointmentsReport(ActionEvent event) throws IOException {
        changeView(event, "/com/schedulingdesktopapp/schedulingdesktopapp/AppointmentsReport.fxml");
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * Helper method
     * Change the view to whatever page given in the resource passed in
     *
     * @param event passed in from the method that this is called from
     * @param resource resource name for the page we would like to navigate to
     * @throws IOException standard
     */
    public void changeView(ActionEvent event, String resource) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(resource));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * HELPER method
     * Log In class calls this method on login to inform patient if they have any upcoming appointments
     */
    public void sendAppointmentAlert() {
        // Lambda expression used to add minutes to the current time
        GeneralInterface tM = tD -> LocalDateTime.now().plusMinutes(tD);
        LocalDateTime dateTimeStart = LocalDateTime.now();
        LocalDateTime dateTimeEnd = tM.getNewTime(15);
        java.sql.Timestamp startTime = GlobalHelperMethods.convertToUTC(dateTimeStart.toLocalDate(), dateTimeStart.toLocalTime());
        java.sql.Timestamp endTime = GlobalHelperMethods.convertToUTC(dateTimeEnd.toLocalDate(), dateTimeEnd.toLocalTime());
        boolean anyAppts = false;
        try {
            // While there are appointments send an alert letting know of any appointments
            ResultSet rs = AppointmentsQuery.selectTimeFrame(startTime, endTime);
            while (rs.next()) {
                GlobalHelperMethods.showInformationAlert("Upcoming Appointment", "You have an upcoming appointment: \n\tID: " + rs.getInt("Appointment_ID") + "\n\t at: " + rs.getTimestamp("Start"));
                anyAppts = true;
            }

            // If there are no appointments then say an alert there are no appointments
            if (!anyAppts) {
                GlobalHelperMethods.showInformationAlert("No Upcoming Appointments", "You have no appointments in the next 15 minutes");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


