package com.schedulingdesktopapp.controller;

import com.schedulingdesktopapp.dao.UsersQuery;
import com.schedulingdesktopapp.helper.GlobalHelperMethods;
import com.schedulingdesktopapp.model.UserLogIns;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

public class LogInScreenController implements Initializable {
    Stage stage;
    Parent scene;


    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label login;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Button signInButton;
    @FXML
    private Label location;
    private String incorrectLoginTitle;
    private String incorrectLoginDescription;

    /**
     * When user clicks sign in check the username and password with the database to see if it's correct
     * Show an error alert if it's incorrect, if it's correct change the view to the main menu
     * Add the login attempt to the login_activity file log
     * @param event event passed in when sign in button is clicked
     * @throws IOException  May throw an IO except because of the change view
     * @throws SQLException May throw an sql exception because of the database query
     */
    @FXML
    void onActionSignIn(ActionEvent event) throws IOException, SQLException {
        // Query the database and confirm if the username and password match a db record, return the user id if correct, if not then return -3
        int passwordMatch = UsersQuery.validateLogin(String.valueOf(username.getText()), String.valueOf(password.getText()));

        // Submit the attempt to the text file
        UserLogIns.submitLoginAttempts(username.getText() + "\t" + password.getText() + "\t" + LocalDateTime.now());

        // If password match equals -3 then it was incorrect, otherwise follow the process for correct
        if (passwordMatch != -3) {
            // Assign the global variable userID to the return value from passwordMatch
            UserLogIns.userID = passwordMatch;

            // Load the main menu and call the method in the main menu controller that will send an alert if there's an appointment within 15 minutes
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/schedulingdesktopapp/schedulingdesktopapp/MainMenu.fxml"));
            loader.load();
            MainMenuController menuController = loader.getController();
            menuController.sendAppointmentAlert();
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        } else {
            // Show error alert for incorrect login information
            GlobalHelperMethods.showErrorAlert(incorrectLoginTitle, incorrectLoginDescription);
            password.setText("");
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) throws RuntimeException {
        // Pull the language bundle and get the default for the system
        rb = ResourceBundle.getBundle("languages/Language", Locale.getDefault());

        // Get the local time zone
        ZoneId localId = ZoneId.systemDefault();

        // Set the values for this page with the default language
        login.setText(rb.getString("Login"));
        usernameLabel.setText(rb.getString("username"));
        passwordLabel.setText(rb.getString("password"));
        signInButton.setText(rb.getString("signIn"));
        location.setText(rb.getString("location") + ": " + localId);
        incorrectLoginTitle = rb.getString("incLoginTitle");
        incorrectLoginDescription = rb.getString("incLoginDesc");
    }


}