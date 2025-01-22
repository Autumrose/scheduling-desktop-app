package com.schedulingdesktopapp;

import java.time.LocalDateTime;

/**
 * Functional interface with one method to convert the time right now for the time difference given
 * Different implementations for the time frame to add
 */
public interface GeneralInterface {
    LocalDateTime getNewTime(int dif);
}
