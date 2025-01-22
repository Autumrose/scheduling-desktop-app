package com.schedulingdesktopapp.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Class to create Appointment objects
 */
public class Appointments {
    private int apptID;
    private String description;
    private String title;
    private String location;
    private String type;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;

    private java.sql.Date createdDate;
    private String createdBy;

    private java.sql.Timestamp updatedLast;
    private String updatedBy;
    private int customerID;
    private int userID;
    private int contactID;
    private String contactName;

    /**
     * constructor for the Appointment class
     * @param apptID,title,description,location, type, startDate, startTime, endDate, endTime, createdDate, createdBy, updatedLast, updatedBy, customerID, userID, contactID, contactName
     *           input parameters to initialize all of the object variables
     */
    public Appointments(int apptID, String title, String description, String location, String type, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, java.sql.Date createdDate, String createdBy,
                        java.sql.Timestamp updatedLast, String updatedBy, int customerID, int userID, int contactID, String contactName) {
        this.apptID = apptID;
        this.description = description;
        this.title = title;
        this.location = location;
        this.type = type;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.updatedLast = updatedLast;
        this.updatedBy = updatedBy;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
        this.contactName = contactName;
    }

    /**
     * Getters and setters for the object class
     * @return
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getApptID() {
        return this.apptID;
    }

    public String getDescription() {
        return this.description;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return this.type;
    }

    public int getCustomerID() {
        return this.customerID;
    }

    public int getUserID() {
        return this.userID;
    }

    public Date getCreatedDate() {
        return createdDate;
    }


    public String getCreatedBy() {
        return createdBy;
    }

    public Timestamp getUpdatedLast() {
        return updatedLast;
    }


    public String getUpdatedBy() {
        return updatedBy;
    }

    public String getContactName() {
        return contactName;
    }

}
