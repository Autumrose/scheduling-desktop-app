General information
Title: Scheduling Desktop Application
Puporse:
Author: Autumrose Stubbs
Author Contact Information (Email): astub60@wgu.edu
Student application version: 1.0 - date: 1/18/2024
IDE version number: IntelliJ IDEA 2023.2.2
Full JDK version:Java SE 17.0.8+7
JavaFX version: JavaFX-SDK-17.0.9
Additional Report: CustomerReportController and CustomerReport.fxml. This report sorts customers into geographical areas first by country and then by first level divisions and counts the total. The report displays each division under the country and how many customers are in that division.
MySQL Connector: mysql-connector-j-8.2.02

Instructions for how to run the program:
1. Run the main application file "LogInScreenApp" in the application package
2. Login with a username and password found in the database in order to access the remainder of the pages
3. On the Main Menu select any of the options to continue to that section. (Please see the descriptions below regarding what each page does)

Files - application package
LogInScreenApp: This contains the main class for the project. Initializes the login screen and connects/disconnects to the database

Files - controller package
AddUpdateAppointmentController: Controller for AddUpdateAppointment.fxml. Form to display, update, and add one selected appointment record
AddUpdateCustomerController: Controller for AddUpdateCustomer.fxml. Form to display, update, and add one selected customer record
AppointmentsController: Controller for Appointments.fxml view. Displays all of the appointments by default, can select month or week and navigate between the time periods. Then select any record and edit, delete, or create new.
AppointmentsReportController: Controller for AppointmentsReport.fxml view. Displays a report that lists the number of appointments of each type as well as by month
ContactRecordController: Controller for ContactRecord.fxml view. Shows all of the contacts, can view the full report and schedule by selecting any contact
ContactsController: Controller for the Contacts.fxml view. Full report and schedule for an individual contact.
CustomerRecordController: Controller for the CustomerRecord.fxml view. Shows all of the customers
CustomerReportController: Controller for the CustomerReport.fxml view. Contains the report for how many customers by division within each country
LogInScreenController: Controller for the LogInScreen.fxml view. Accepts a username and password, if accepted allows the user into the application
MainMenuController: Controller for the MainMenu.fxml view. Has a path to Appointments and Customers. As well as the 3 reports - Contacts, Appointments, and Customers Reports.

Files - dao package
AppointmentsQuery - All sql queries to the Appointments table
ContactsQuery - All sql queries to the Contacts table
CountriesQuery - All sql queries to the Countries table
CustomersQuery - All sql queries to the Customers table
DivisionsQuery - All sql queries to the First_Level_Divisions table
UsersQuery - All sql queries to the Users table


Files - helper package
GlobalHelperMethods - Contains methods that can be utilized by multiple classes
JDBC - methods to open and close connection with the database

Files - model package
Appointments - Stores appointment information
Contacts - Stores contact information
Customer - Stores customer information
UserLogIns - Store user ID and push all login attempts to the text file

GeneralInterface - Functional interface with one method to get a new time. Used in 3 different lambda expressions.