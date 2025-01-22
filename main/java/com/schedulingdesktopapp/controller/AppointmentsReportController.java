package com.schedulingdesktopapp.controller;

import com.schedulingdesktopapp.dao.AppointmentsQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AppointmentsReportController implements Initializable {
    Stage stage;
    Parent scene;
    @FXML
    private TextArea typeTextArea;
    @FXML
    private TextArea monthTextArea;

    /**
     * Change the view back to the main menu when the menu button is clicked
     * @param event event passed in when menu button is clicked
     * @throws IOException  May throw an IO except because of the view change
     */
    @FXML
    public void onActionClickMenu(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/com/schedulingdesktopapp/schedulingdesktopapp/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) throws RuntimeException {

        // Select all appointments sorted by type
        try {
            ResultSet types = AppointmentsQuery.selectTypeValues();
            // Iterate through the appointments and keep track of how many of each type
            while (types.next()) {
                ResultSet appts = AppointmentsQuery.selectAllAppointmentsType(types.getString("Type"));
                int count = 0;
                while (appts.next()) {
                    count++;
                }
                // Append the type name and number to the text area
                typeTextArea.appendText(types.getString("Type") + ": " + count + "\n");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Select all appointments sorted by date
        try {
            ResultSet appts = AppointmentsQuery.selectSorted();
            String currMonth = "";
            int count = 0;
            // Iterate through the appointments and keep track of how many in each month
            while (appts.next()) {
                LocalDate apptStart = appts.getTimestamp("Start").toLocalDateTime().toLocalDate();
                // Set the first element
                if (currMonth.isEmpty()) {
                    currMonth = apptStart.getMonth().toString() + " " + apptStart.getYear();
                }
                // Once you reach a new month, append the previous month and count to the month text area
                if (!currMonth.equals(apptStart.getMonth().toString() + " " + apptStart.getYear())) {
                    monthTextArea.appendText(currMonth + ": " + count + "\n");
                    currMonth = apptStart.getMonth().toString() + " " + apptStart.getYear();
                    count = 0;
                }
                count++;
            }
            monthTextArea.appendText(currMonth + ": " + count + "\n");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
