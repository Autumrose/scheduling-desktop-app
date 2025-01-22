package com.schedulingdesktopapp.model;

import java.sql.Timestamp;

public class Customer {

    private int id;
    private String name;
    private String address;
    private String postal;
    private String phone;
    private Timestamp createdDate;
    private String createdBy;
    private Timestamp updatedDate;
    private String updatedBy;
    private final int divisionID;

    /**
     * constructor for the Customer class
     * @param id,name,address,postal, phone, createdDate, createdBy, updatedDate, updatedBy, divisionID
     *           input parameters to initialize all of the object variables
     */

    public Customer(int id, String name, String address, String postal, String phone, java.sql.Timestamp createdDate, String createdBy, java.sql.Timestamp updatedDate, String updatedBy, int divisionID) {

        this.id = id;
        this.name = name;
        this.address = address;
        this.postal = postal;
        this.phone = phone;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.updatedDate = updatedDate;
        this.updatedBy = updatedBy;
        this.divisionID = divisionID;
    }

    /**
     * Getter method for the division id
     * @return divisionID
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     * Getter method for the updated by variable
     * @return updatedBy
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     * Getter method for the updated date variable
     * @return updatedDate
     */
    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    /**
     * Getter method for the id variabe
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Getter method for name variable
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter variable for address variable
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Getter for the postal variable
     * @return postal
     */
    public String getPostal() {
        return postal;
    }

    /**
     * Getter for the phone variable
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Getter for the created date variable
     * @return createdDate
     */
    public Timestamp getCreatedDate() {
        return createdDate;
    }

    /**
     * Getter for the created by variable
     * @return createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }
}
