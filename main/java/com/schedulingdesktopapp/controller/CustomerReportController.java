package com.schedulingdesktopapp.controller;

import com.schedulingdesktopapp.dao.CustomersQuery;
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
import java.util.ResourceBundle;

public class CustomerReportController implements Initializable {
    Stage stage;
    Parent scene;
    @FXML
    private TextArea reportTextArea;

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
        try {
            // Select all of the customers sorted by location
            ResultSet rs = CustomersQuery.selectSortByLocation();
            rs.next();

            // Iterate through all of the customers and track how many in each division
            String country = rs.getString("Country");
            String div = rs.getString("Division");
            reportTextArea.appendText(country + "\n\t" + div + ": ");
            int custCount = 0;
            while (rs.next()) {
                custCount++;
                if (!div.equals(rs.getString("Division"))) {
                    // Append the number of customers in the division
                    reportTextArea.appendText(String.valueOf(custCount));
                    if (!country.equals(rs.getString("Country"))) {
                        // Append the name of the next country
                        country = rs.getString("Country");
                        reportTextArea.appendText("\n" + country);
                    }

                    // Reset the count and append the next division
                    custCount = 0;
                    div = rs.getString("Division");
                    reportTextArea.appendText("\n\t" + div + ": ");
                }
            }
            custCount++;
            reportTextArea.appendText(String.valueOf(custCount));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
