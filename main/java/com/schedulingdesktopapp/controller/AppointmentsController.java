package com.schedulingdesktopapp.controller;

import com.schedulingdesktopapp.GeneralInterface;
import com.schedulingdesktopapp.dao.AppointmentsQuery;
import com.schedulingdesktopapp.dao.ContactsQuery;
import com.schedulingdesktopapp.helper.GlobalHelperMethods;
import com.schedulingdesktopapp.model.Appointments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class AppointmentsController implements Initializable {
    Stage stage;
    Parent scene;
    @FXML
    private Label currTimeFrame;
    @FXML
    private TableView<Appointments> appointmentMonthTableView;
    @FXML
    private TableColumn<Appointments, Integer> idColMonth, customerIDColMonth, userIDColMonth;
    @FXML
    private TableColumn<Appointments, String> titleColMonth, descColMonth, locationColMonth, contactColMonth, typeColMonth;
    @FXML
    private TableColumn<Appointments, LocalDate> startDateColMonth, endDateColMonth;
    @FXML
    private TableColumn<Appointments, LocalTime> startTimeColMonth, endTimeColMonth;
    @FXML
    private Tab monthTab;

    @FXML
    private TableView<Appointments> appointmentWeekTableView;
    @FXML
    private TableColumn<Appointments, Integer> idColWeek, customerIDColWeek, userIDColWeek;
    @FXML
    private TableColumn<Appointments, String> titleColWeek, descColWeek, locationColWeek, contactColWeek, typeColWeek;
    @FXML
    private TableColumn<Appointments, LocalDate> startDateColWeek, endDateColWeek;
    @FXML
    private TableColumn<Appointments, LocalTime> startTimeColWeek, endTimeColWeek;
    @FXML
    private Tab weekTab;

    @FXML
    private TableView<Appointments> appointmentAllTableView;
    @FXML
    private TableColumn<Appointments, Integer> idColAll, customerIDColAll, userIDColAll;
    @FXML
    private TableColumn<Appointments, String> titleColAll, descColAll, locationColAll, contactColAll, typeColAll;
    @FXML
    private TableColumn<Appointments, LocalDate> startDateColAll, endDateColAll;
    @FXML
    private TableColumn<Appointments, LocalTime> startTimeColAll, endTimeColAll;

    @FXML
    private Button previousButton;
    @FXML
    private Button nextButton;


    private int weekCount;
    private int monthCount;

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
     * Change the view to the add update or appointment view where user can edit the fields
     * @param event event passed in when new appointment button is clicked
     * @throws IOException  May throw an IO except because of the change view
     */
    @FXML
    void onActionClickNewAppointment(ActionEvent event) throws IOException {
        changeView(event, "/com/schedulingdesktopapp/schedulingdesktopapp/AddUpdateAppointment.fxml");
    }

    /**
     * Change the view to the add update or appointment view where user can edit the fields
     * Send the existing appointment information to the fields to prepopulate
     * @param event event passed in when update button is clicked
     * @throws IOException  May throw an IO except because of the change view
     * @throws SQLException May throw an sql exception because of the database query
     */
    @FXML
    void onActionClickUpdateAppointment(ActionEvent event) throws IOException, SQLException {
        Appointments row;
        // Assign the row based on what tab is selected
        if (weekTab.isSelected()) {
            row = appointmentWeekTableView.getSelectionModel().getSelectedItem();
        } else if (monthTab.isSelected()) {
            row = appointmentMonthTableView.getSelectionModel().getSelectedItem();
        } else {
            row = appointmentAllTableView.getSelectionModel().getSelectedItem();
        }

        // if a row isn't selected then show an alert to select a row firsrt
        if (row == null) {
            GlobalHelperMethods.showErrorAlert("Please select a row", "Please select an item from the table, if no items available please click Add to create new ones.");
            return;
        }

        // Change view and send the current appointment information over to the new view
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/schedulingdesktopapp/schedulingdesktopapp/AddUpdateAppointment.fxml"));
        loader.load();
        AddUpdateAppointmentController apptController = loader.getController();
        apptController.sendAppointmentInfo(row);
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Delete the selected row from the database and update the table
     * @param event event passed in when delete button is clicked
     * @throws IOException  May throw an IO except because of the change view
     * @throws SQLException May throw an sql exception because of the database query
     */
    @FXML
    void onActionClickDeleteAppointment(ActionEvent event) throws IOException, SQLException {
        Appointments row;
        // Assign the row based on what tab is selected
        if (weekTab.isSelected()) {
            row = appointmentWeekTableView.getSelectionModel().getSelectedItem();
        } else if (monthTab.isSelected()) {
            row = appointmentMonthTableView.getSelectionModel().getSelectedItem();
        } else {
            row = appointmentAllTableView.getSelectionModel().getSelectedItem();
        }

        // if a row isn't selected then show an alert to select a row firsrt
        if (row == null) {
            GlobalHelperMethods.showErrorAlert("Please select a row", "Please select an item from the table, if no items available please click Add to create new ones.");
            return;
        }
        // Confirm with the user if they want to delete the selected appointment
        Optional<ButtonType> result = GlobalHelperMethods.showConfirmationAlert("Delete Selected Appointment", "Are you sure you would like to delete this appointment?: \n\tAppointment ID: \t" + row.getApptID() + "\n\tOn : \t\t\t\t" + row.getStartDate() + " on " + row.getEndDate() + "\n\tType: \t\t\t" + row.getType());
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Remove the selected item from the correct table based on what tab is selected
            AppointmentsQuery.delete(row.getApptID());
            if (weekTab.isSelected()) {
                appointmentWeekTableView.getItems().remove(row);
            } else if (monthTab.isSelected()) {
                appointmentMonthTableView.getItems().remove(row);
            } else {
                appointmentAllTableView.getItems().remove(row);
            }

            // Inform the user they successfully deleted the appointment
            GlobalHelperMethods.showInformationAlert("Appointment cancellation successful", "You have successful deleted appointment:\n\tAppointment ID: \t" + row.getApptID() + "\n\tOn : \t\t\t\t" + row.getStartDate() + " on " + row.getEndDate() + "\n\tType: \t\t\t" + row.getType());
        }

    }

    /**
     *
     * @param event event passed in when month tab is clicked
     * @throws SQLException May throw an sql exception because of the database query
     */
    @FXML
    public void onSelectionMonth(Event event) throws SQLException {
        setButtonVisibility(true);

        // Reset our count variables to track how many months out we are
        monthCount = 1;
        weekCount = 0;

        // Set the table columns, find the list in the given time period use timePeriod method
        setTableColumns(idColMonth, descColMonth, titleColMonth, locationColMonth, contactColMonth, typeColMonth,
                startDateColMonth, startTimeColMonth, endDateColMonth, endTimeColMonth, customerIDColMonth,
                userIDColMonth, timePeriod(appointmentMonthTableView, monthCount, false), appointmentMonthTableView);
    }

    /**
     * Method for when the week tab is selected, populate the table loaded in the week tab
     * @param event event passed in when week tab is clicked
     * @throws SQLException May throw an sql exception because of the database query
     */
    public void onSelectionWeek(Event event) throws SQLException {
        setButtonVisibility(true);

        // Reset our count variables to track how many weeks out we are
        weekCount = 1;
        monthCount = 0;

        // Set the table columns, find the list in the given time period use timePeriod method
        setTableColumns(idColWeek, descColWeek, titleColWeek, locationColWeek, contactColWeek, typeColWeek,
                startDateColWeek, startTimeColWeek, endDateColWeek, endTimeColWeek, customerIDColWeek, userIDColWeek,
                timePeriod(appointmentWeekTableView, weekCount, true), appointmentWeekTableView);
    }

    /**
     * Method for when the all tab is selected, populate the table loaded in the all tab
     * @param event event passed in when all tab is clicked
     * @throws SQLException May throw an sql exception because of the database query
     */
    @FXML
    public void onSelectionAll(Event event) throws SQLException {
        // Reset our count variables
        monthCount = 0;
        weekCount = 0;

        // Select all appointments as there's no timeframe
        ResultSet rs = AppointmentsQuery.select();

        // Set the table columns
        setTableColumns(idColAll, descColAll, titleColAll, locationColAll, contactColAll, typeColAll, startDateColAll,
                startTimeColAll, endDateColAll, endTimeColAll, customerIDColAll, userIDColAll, appointmentData(rs), appointmentAllTableView);
    }

    /**
     * Method for when the previous button is clicked, changes the time frame to a week or month prior depending on what tab we are in
     * @param event event passed in when previous button is clicked
     * @throws SQLException May throw an sql exception because of the database query
     */
    @FXML
    private void onActionClickPrevious(ActionEvent event) throws SQLException {
        // Check if the week tab or month tab is selected
        if (weekTab.isSelected()) {
            // If the count is greater than 1 then decrease the count, otherwise stay the same as it's on today's date
            if (weekCount > 1) {
                weekCount--;
            }
            // Reset the table
            timePeriod(appointmentWeekTableView, weekCount, true);
        } else if (monthTab.isSelected()) {
            // If the count is greater than 1 then decrease the count, otherwise stay the same as it's on today's date
            if (monthCount > 1) {
                monthCount--;
            }
            // Reset the table
            timePeriod(appointmentMonthTableView, monthCount, false);
        }
    }
    /**
     * Method for when the next button is clicked, changes the time frame to a week or month after depending on what tab we are in
     * @param event event passed in when next button is clicked
     * @throws SQLException May throw an sql exception because of the database query
     */
    @FXML
    private void onActionClickNext(ActionEvent event) throws SQLException {
        // Check if the week tab or month tab is selected
        if (weekTab.isSelected()) {
            weekCount++;
            // Reset the table
            timePeriod(appointmentWeekTableView, weekCount, true);
        } else if (monthTab.isSelected()) {
            monthCount++;
            // Reset the table
            timePeriod(appointmentMonthTableView, monthCount, false);
        }
    }


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) throws RuntimeException {
        setButtonVisibility(false);
        monthCount = 0;
        weekCount = 0;

        // Set up the table for all appointments
        try {
            ResultSet rs = AppointmentsQuery.select();
            setTableColumns(idColAll, descColAll, titleColAll, locationColAll, contactColAll, typeColAll, startDateColAll,
                    startTimeColAll, endDateColAll, endTimeColAll, customerIDColAll, userIDColAll, appointmentData(rs), appointmentAllTableView);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * Helper method
     * Set the table to only show the required period (week, month, etc) from today's date
     *
     * Lambda justification: 2 uses of the functional interface GeneralInterface - getNewTimeCount
     * This is helpful as I used it with 2 different implementations in this method and 1 other in the Main Menu class,
     * each one converts the time right now and adjusts is based on the implementation by adding weeks, months, minutes
     * How many of each unit will be based on the input in the implementation
     *
     * @param table which table we are editing - month or week
     * @param count which count variable to use to determine how far out to show
     * @param week  boolean signifies if we should add a week or if not then we do a month
     */
    public ObservableList<Appointments> timePeriod(TableView<Appointments> table, int count, boolean week) throws SQLException {
        // Use Lambda expression to translate date to weeks in the future using the input int
        GeneralInterface tW = tD -> LocalDateTime.now().plusWeeks(tD);
        // Use Lambda expression to translate date to months in the future using the input int
        GeneralInterface tM = tD -> LocalDateTime.now().plusMonths(tD);
        LocalDateTime dateTimeStart;
        LocalDateTime dateTimeEnd;
        if (week) {
            dateTimeStart = tW.getNewTime(count - 1);
            dateTimeEnd = tW.getNewTime(count);
        } else {
            dateTimeStart = tM.getNewTime(count - 1);
            dateTimeEnd = tM.getNewTime(count);
        }
        java.sql.Timestamp startDate = java.sql.Timestamp.valueOf(dateTimeStart);
        java.sql.Timestamp endDate = java.sql.Timestamp.valueOf(dateTimeEnd);

        // Change the text on the time frame to reflect the search period
        currTimeFrame.setText(dateTimeStart.toLocalDate().toString() + "  to  " + dateTimeEnd.toLocalDate().toString());
        ObservableList<Appointments> data = appointmentData(AppointmentsQuery.selectTimeFrame(startDate, endDate));

        // Update the table to reflect the appointments in this timeframe
        table.setItems(data);
        return data;
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
     * Create the appointment objects from the data pulled from the database
     *
     * @param rs result set returned from the database, already filtered by day if needed
     */
    public ObservableList<Appointments> appointmentData(ResultSet rs) {
        ObservableList<Appointments> data = FXCollections.observableArrayList();
        try {
            // Get the results within the timeframe
            while (rs.next()) {
                // Convert the timestampt to local date time then use the time zone to get the zoned time
                ZonedDateTime start = convertedTime(rs.getTimestamp("Start"));
                ZonedDateTime end = convertedTime(rs.getTimestamp("End"));

                // Create a new Appointment object with the result set current row information
                Appointments appt = new Appointments(rs.getInt("Appointment_ID"), rs.getString("Title"), rs.getString("Description"), rs.getString("Location"), rs.getString("Type"), start.toLocalDate(), start.toLocalTime(), end.toLocalDate(), end.toLocalTime(), rs.getDate("Create_Date"), rs.getString("Created_By"), rs.getTimestamp("Last_Update"), rs.getString("Last_Updated_By"), rs.getInt("Customer_ID"), rs.getInt("User_ID"), rs.getInt("Contact_ID"), ContactsQuery.selectName(rs.getInt("Contact_ID")));
                data.add(appt);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    /**
     * Method to set all of the columns to the values given for the Appointment object
     * Pass in the values so each table can use the same method with different column names
     *
     * @param id, desc, title, location, contactName, type, sD, sT, eD, eT, cID, uID, data, tv
     *            Table, columns, and list. So each view can use the same method
     */
    private void setTableColumns(TableColumn<Appointments, Integer> id, TableColumn<Appointments, String> desc,
                                 TableColumn<Appointments, String> title, TableColumn<Appointments, String> location,
                                 TableColumn<Appointments, String> contactName, TableColumn<Appointments, String> type,
                                 TableColumn<Appointments, LocalDate> sD, TableColumn<Appointments, LocalTime> sT,
                                 TableColumn<Appointments, LocalDate> eD, TableColumn<Appointments, LocalTime> eT,
                                 TableColumn<Appointments, Integer> cID, TableColumn<Appointments, Integer> uID,
                                 ObservableList<Appointments> data, TableView<Appointments> tv) {
        tv.setItems(data);
        id.setCellValueFactory(new PropertyValueFactory<>("apptID"));
        desc.setCellValueFactory(new PropertyValueFactory<>("description"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        location.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactName.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        sD.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        sT.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        eD.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        eT.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        cID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        uID.setCellValueFactory(new PropertyValueFactory<>("userID"));
    }

    /**
     * Convert the time from UTC to local time
     *
     * @param dateTime contains the date and time to convert
     * @return ZonedDateTime object to retrieve the local date and time from
     */
    private ZonedDateTime convertedTime(java.sql.Timestamp dateTime) {
        ZoneId utcId = ZoneId.of("UTC");
        ZoneId localId = ZoneId.systemDefault();

        ZonedDateTime utcZone = ZonedDateTime.of(dateTime.toLocalDateTime(), utcId);
        return ZonedDateTime.ofInstant(utcZone.toInstant(), localId);

    }

    /**
     * Set the previous and next buttons' visibility
     * On all appointments set to not visibile, true visible for month/row to navigate between
     *
     * @param on whether to set the buttons as visible or not visible
     */
    private void setButtonVisibility(Boolean on) {
        previousButton.setVisible(on);
        nextButton.setVisible(on);
    }
}
