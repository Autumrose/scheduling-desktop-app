package com.schedulingdesktopapp.helper;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.time.*;
import java.util.Optional;

/**
 * Global helper methods
 */
public class GlobalHelperMethods{

    /**
     * Method to show an error alert pop up
     *
     * @param title   the title of the alert pop up
     * @param message the full message to include in the alert
     */
    public static void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Method to show an informational message alert pop up
     *
     * @param title   the title of the alert pop up
     * @param message the full message to include in the alert
     */
    public static void showInformationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Method to show a confirmation alert
     *
     * @param title   the title of the alert pop up
     * @param message the full message to include in the alert
     * @return the button the user selected
     */
    public static Optional<ButtonType> showConfirmationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        return alert.showAndWait();
    }

    /**
     * Method to convert the time from local time to UTC time zone
     * @param date local date to convert
     * @param time local time to convert
     * @return the timestamp in UTC
     */
    public static java.sql.Timestamp convertToUTC(LocalDate date, LocalTime time) {
        ZoneId localId = ZoneId.systemDefault();

        ZoneId utcId = ZoneId.of("UTC");

        ZonedDateTime localZone = ZonedDateTime.of(LocalDateTime.of(date, time), localId);

        return java.sql.Timestamp.valueOf(ZonedDateTime.ofInstant(localZone.toInstant(), utcId).toLocalDateTime());
    }

}
