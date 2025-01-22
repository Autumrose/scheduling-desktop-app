package com.schedulingdesktopapp.model;

/**
 * Class to create Contact objects
 */
public class Contacts {
    private int id;
    private String name;
    private String email;

    /**
     * constructor for the Appointment class
     * @param id, name, email
     *         input parameters to initialize all of the object variables
     */
    public Contacts(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    /**
     * id getter method
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * id setter method
     * @param id take the value to update it to
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * name getter method
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * name setter method
     * @param name value to update name to
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method for email
     * @return the object's email
     */
    public String getEmail() {
        return email;
    }
}
