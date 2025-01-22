package com.schedulingdesktopapp.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Class to store the current user ID and push any login attempts
 */
public class UserLogIns {

    public static int userID;

    /**
     * Append the given login attempt to the text file
     * @param attempt contains the information of the attempt to store
     * @throws IOException may be thrown for the sequel query
     */
    public static void submitLoginAttempts(String attempt) throws IOException {
        File log = new File("login_activity.txt");
        log.createNewFile();

        PrintWriter out = new PrintWriter(new FileWriter(log, true));
        out.append(attempt + "\n");
        out.close();
    }

}
