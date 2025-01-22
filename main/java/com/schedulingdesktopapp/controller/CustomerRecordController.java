package com.schedulingdesktopapp.controller;

import com.schedulingdesktopapp.dao.AppointmentsQuery;
import com.schedulingdesktopapp.dao.CustomersQuery;
import com.schedulingdesktopapp.helper.GlobalHelperMethods;
import com.schedulingdesktopapp.model.Customer;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerRecordController implements Initializable {

    Stage stage;
    Parent scene;
    @FXML
    private TableColumn<Customer, Integer> id, divisionID;
    @FXML
    private TableColumn<Customer, String> name, address, postal, phone, createdBy, updatedBy;
    @FXML
    private TableColumn<Customer, java.sql.Timestamp> createdDate, updatedDate;
    @FXML
    private TableView<Customer> tableView;

    /**
     * Option to return to the main menu, changes the view to the fxml file for the menu
     * @param event event passed in when menu button is clicked
     * @throws IOException  May throw an IO except because of the change view
     * @throws SQLException May throw an sql exception because of the database query
     */
    @FXML
    void onActionClickMenu(ActionEvent event) throws IOException, SQLException {
        changeView(event, "/com/schedulingdesktopapp/schedulingdesktopapp/MainMenu.fxml");
    }

    /**
     * Change the view to the add update customer view where user can edit the fields
     * @param event event passed in when new customer button is clicked
     * @throws IOException  May throw an IO except because of the change view
     */
    @FXML
    void onActionClickAdd(ActionEvent event) throws IOException, SQLException {
        changeView(event, "/com/schedulingdesktopapp/schedulingdesktopapp/AddUpdateCustomer.fxml");
    }

    /**
     * Change the view to the add update customer view where user can edit the fields
     * Send the existing customer information to the fields to prepopulate
     * @param event event passed in when update button is clicked
     * @throws IOException  May throw an IO except because of the change view
     * @throws SQLException May throw an sql exception because of the database query
     */
    @FXML
    void onActionClickUpdate(ActionEvent event) throws IOException, SQLException {
        // Get an object for a customer for the selected row
        Customer row = tableView.getSelectionModel().getSelectedItem();

        // If the row is null then show an alert telling the patient to select a row
        if (row == null) {
            GlobalHelperMethods.showErrorAlert("Please select a row", "Please select an item from the table, if no items available please click Add to create new ones.");
        } else {
            // Otherwise send the customer information over from the selected row and change scene into the add update customer view
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/schedulingdesktopapp/schedulingdesktopapp/AddUpdateCustomer.fxml"));
            loader.load();
            AddUpdateCustomerController custController = loader.getController();
            custController.sendCustomerInformation(row);
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * Delete the selected row from the database and update the table
     * @param event event passed in when delete button is clicked
     * @throws IOException  May throw an IO except because of the change view
     * @throws SQLException May throw an sql exception because of the database query
     */
    @FXML
    void onActionClickDelete(ActionEvent event) throws IOException, SQLException {
        // Get an object for a customer for the selected row
        Customer row = tableView.getSelectionModel().getSelectedItem();

        // If the row is null then show an alert telling the patient to select a row
        if (row == null) {
            GlobalHelperMethods.showErrorAlert("Please select a row", "Please select an item from the table, if no items available please click Add to create new ones.");
        } else {
            // Confirm if the patient would like to delete the selected record
            Optional<ButtonType> result = GlobalHelperMethods.showConfirmationAlert("Delete Selected Customer", "Are you sure you would like to delete the selected customer?");
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Delete all of the customer's appointments first before deleting the record
                ResultSet rs = AppointmentsQuery.selectForCustomer(row.getId());
                String deletedAppts = "";
                while (rs.next()) {
                    deletedAppts += "\n\t" + rs.getInt("Appointment_ID") + " on " + rs.getTimestamp("Start");
                }
                // Delete the selected customer from the database and table
                AppointmentsQuery.deleteCustomerAppointments(row.getId());
                CustomersQuery.delete(row.getId());
                tableView.getItems().remove(row);

                // Show confirmation the selected record was deleted
                GlobalHelperMethods.showInformationAlert("Customer Record Deleted", "You have successful deleted the customer record:\n\tName:\t" + row.getName() + "\n\tID:\t\t" + row.getId() + "\nDeleted customer's appointments: " + deletedAppts);
            }
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) throws RuntimeException {
        ObservableList<Customer> data = FXCollections.observableArrayList();

        try {
            // Select all of the customers and create customer objects with the information
            ResultSet weekRS = CustomersQuery.select();
            while (weekRS.next()) {
                Customer customer = new Customer(weekRS.getInt("Customer_ID"), weekRS.getString("Customer_Name"), weekRS.getString("Address"), weekRS.getString("Postal_Code"), weekRS.getString("Phone"), weekRS.getTimestamp("Create_Date"), weekRS.getString("Created_By"), weekRS.getTimestamp("Last_Update"), weekRS.getString("Last_Updated_By"), weekRS.getInt("Division_ID"));
                data.add(customer);
            }

            // Set the table to the list of customer objects and assign the columns
            tableView.setItems(data);
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            name.setCellValueFactory(new PropertyValueFactory<>("name"));
            address.setCellValueFactory(new PropertyValueFactory<>("address"));
            postal.setCellValueFactory(new PropertyValueFactory<>("postal"));
            phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
            createdDate.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
            createdBy.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
            updatedDate.setCellValueFactory(new PropertyValueFactory<>("updatedDate"));
            updatedBy.setCellValueFactory(new PropertyValueFactory<>("updatedBy"));
            divisionID.setCellValueFactory(new PropertyValueFactory<>("divisionID"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
}
