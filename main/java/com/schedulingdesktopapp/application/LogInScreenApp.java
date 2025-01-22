package com.schedulingdesktopapp.application;

import com.schedulingdesktopapp.helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

/**
 * Main application class, starts program and connects to the database
 */
public class LogInScreenApp extends Application {

    /**
     * Main method, opens the connection with the database, launches the program, and closes the DB connection
     *
     */
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        // Uncomment line to set system default language to French to test the French translations
            //Locale.setDefault(new Locale("fr", "FR"));
        JDBC.openConnection();
        launch();
        JDBC.closeConnection();
    }

    /**
     * Starts your JavaFX program, initializes the Log In screen by loading the fxml file
     *
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LogInScreenApp.class.getResource("/com/schedulingdesktopapp/schedulingdesktopapp/LogInScreenView.fxml"));
        // Set the scene size
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        // Set the title
        stage.setTitle("Scheduling Application");
        stage.setScene(scene);
        stage.show();
    }
}