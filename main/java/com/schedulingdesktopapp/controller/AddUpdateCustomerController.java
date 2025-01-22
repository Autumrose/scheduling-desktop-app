package com.schedulingdesktopapp.controller;

import com.schedulingdesktopapp.dao.CountriesQuery;
import com.schedulingdesktopapp.dao.CustomersQuery;
import com.schedulingdesktopapp.dao.DivisionsQuery;
import com.schedulingdesktopapp.dao.UsersQuery;
import com.schedulingdesktopapp.helper.GlobalHelperMethods;
import com.schedulingdesktopapp.model.Customer;
import com.schedulingdesktopapp.model.UserLogIns;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Class to control the view for the form to update or add a customer to the database
 */
public class AddUpdateCustomerController implements Initializable {
    Stage stage;
    Parent scene;
    @FXML
    private TextField id, name, address, postal, phone, createdOn, createdBy, updatedOn, updatedBy;
    @FXML
    private ComboBox<String> country, division;
    private Boolean newCustomer;

    /**
     * When a country is selected populate the division combo box with the divisions linked to the selected country
     *
     * @param event input event from when the option is selected
     * @throws SQLException may throw an exception for the call to the database
     */
    @FXML
    void onActionClickCountry(ActionEvent event) throws SQLException {
        if (country.getValue() != null) {
            setUpDivisions();
        }
    }

    /**
     * Save or update the customer record based on if it's a new or already existing record
     * After saving/updating then return to the view with all of the appointments
     *
     * @param event event passed in when the option is selected
     * @throws IOException  May throw an IO except because of the change view
     * @throws SQLException May throw an sql exception because of the database query
     */
    @FXML
    void onActionClickSave(ActionEvent event) throws IOException, SQLException {
        // Initialize current time and the logged in user
        LocalDateTime localNow = LocalDateTime.now();
        java.sql.Timestamp now = GlobalHelperMethods.convertToUTC(localNow.toLocalDate(), localNow.toLocalTime());
        String userName = UsersQuery.selectName(UserLogIns.userID);
        // If the customer is new then created a new record in the DB using INSERT
        if (newCustomer) {
            CustomersQuery.insert(String.valueOf(name.getText()), String.valueOf(address.getText()),
                    String.valueOf(postal.getText()), String.valueOf(phone.getText()), now, userName, now, userName,
                    DivisionsQuery.selectDivisionID(String.valueOf(division.getValue())));
        // If customer already exists, update the current record with the new values
        } else {
            CustomersQuery.update(String.valueOf(name.getText()), String.valueOf(address.getText()),
                    String.valueOf(postal.getText()), String.valueOf(phone.getText()),
                    DivisionsQuery.selectDivisionID(String.valueOf(division.getValue())), now, userName,
                    Integer.parseInt(id.getText()));
        }

        // Once completed then change view back to customer record view to see all customers
        changeView(event, "/com/schedulingdesktopapp/schedulingdesktopapp/CustomerRecord.fxml");
    }

    /**
     * If user clicks cancel, confirm they would like to leave the page then return to the appointments screen
     * @param event event for the action of clicking the cancel button
     * @throws IOException  May throw an IO except because of the change view
     */
    @FXML
    void onActionClickCancel(ActionEvent event) throws IOException {
        // Use global helper to send the confirmation alert
        Optional<ButtonType> result = GlobalHelperMethods.showConfirmationAlert("Cancel Creating an Account", "Are you sure you would like to exit the customer creation window? All information will be lost");

        // If the user answers yes then return to the appointments page
        if (result.isPresent() && result.get() == ButtonType.OK) {
            changeView(event, "/com/schedulingdesktopapp/schedulingdesktopapp/CustomerRecord.fxml");
        }
    }


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) throws RuntimeException {
        // Set the combo box with the list of countries
        ObservableList<String> countries = FXCollections.observableArrayList();
        try {
            ResultSet rs = CountriesQuery.select();
            while (rs.next()) {
                countries.add(rs.getString("Country"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        country.setItems(countries);
        ObservableList<String> divisions = FXCollections.observableArrayList();

        // Set the division to instruct the user to select a country first before assigning the division lists
        divisions.add("Please select a country");
        division.setItems(divisions);

        // Set new customer to true by default, change to false only if information was sent over
        newCustomer = true;
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
     * Helper method to set the customer information from CustomerController on an update
     * @param row input customer object row that contains customer information to populate the fields
     * @throws SQLException May throw an sql exception because of the database query
     */
    public void sendCustomerInformation(Customer row) throws SQLException {
        newCustomer = false;
        id.setText(String.valueOf(row.getId()));
        name.setText(row.getName());
        address.setText(row.getAddress());
        postal.setText(row.getPostal());
        phone.setText(row.getPhone());
        createdOn.setText(String.valueOf(row.getCreatedDate()));
        createdBy.setText(String.valueOf(row.getCreatedBy()));
        updatedOn.setText(String.valueOf(row.getUpdatedDate()));
        updatedBy.setText(String.valueOf(row.getUpdatedBy()));
        country.setValue(CountriesQuery.selectCountryName(DivisionsQuery.selectCountryID(row.getDivisionID())));
        division.setValue(DivisionsQuery.selectDivisionName(row.getDivisionID()));
        setUpDivisions();
    }

    /**
     * Set the division combo box to only include divisions from the selected country
     */
    public void setUpDivisions() {
        ObservableList<String> divisions = FXCollections.observableArrayList();
        try {
            ResultSet rs = DivisionsQuery.selectForCountry(CountriesQuery.selectCountryID(String.valueOf(country.getValue())));
            while (rs.next()) {
                divisions.add(rs.getString("Division"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        division.setItems(divisions);
    }
}
