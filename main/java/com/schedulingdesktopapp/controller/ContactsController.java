package com.schedulingdesktopapp.controller;

import com.schedulingdesktopapp.dao.ContactsQuery;
import com.schedulingdesktopapp.helper.GlobalHelperMethods;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ContactsController implements Initializable {
    Stage stage;
    Parent scene;
    @FXML
    private TableView<Contacts> tableView;
    @FXML
    private TableColumn<Contacts, Integer> id;
    @FXML
    private TableColumn<Contacts, String> name, email;

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
     *
     * @param event event passed in when open record button is clicked
     * @throws IOException  May throw an IO except because of the change view
     * @throws SQLException May throw an sql exception because of the database query
     */
    @FXML
    void onActionClickOpenRecord(ActionEvent event) throws IOException, SQLException {
        Contacts row = tableView.getSelectionModel().getSelectedItem();
        // if a row isn't selected then show an alert for the user to select one
        if (row == null) {
            GlobalHelperMethods.showErrorAlert("Please select a row", "Please select an item from the table to view the schedule and record");
        // Otherwise load the contact's information into the contact Record view
        } else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/schedulingdesktopapp/schedulingdesktopapp/ContactRecord.fxml"));
            loader.load();
            ContactRecordController contactController = loader.getController();
            contactController.sendContactInfo(row);
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }


    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Contacts> allData = FXCollections.observableArrayList();

        try {
            // Select all of the contacts
            ResultSet weekRS = ContactsQuery.select();
            while (weekRS.next()) {
                // Create a new contact object with the result set current row information
                Contacts contact = new Contacts(weekRS.getInt("Contact_ID"), weekRS.getString("Contact_Name"), weekRS.getString("Email"));
                allData.add(contact);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Set the table view and assign the columns
        tableView.setItems(allData);
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));

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
