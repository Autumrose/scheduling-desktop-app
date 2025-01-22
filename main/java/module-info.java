module com.schedulingdesktopapp.schedulingdesktopapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.schedulingdesktopapp.application to javafx.fxml;
    exports com.schedulingdesktopapp.application;
    opens com.schedulingdesktopapp.controller to javafx.fxml;
    exports com.schedulingdesktopapp.controller;
    opens com.schedulingdesktopapp.dao to javafx.fxml;
    exports com.schedulingdesktopapp.dao;
    opens com.schedulingdesktopapp.helper to javafx.fxml;
    exports com.schedulingdesktopapp.helper;
    opens com.schedulingdesktopapp.model to javafx.fxml;
    exports com.schedulingdesktopapp.model;
}