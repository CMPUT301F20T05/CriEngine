package com.example.criengine.Objects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Notification class. Creates a notification with a description and a date-of-creation.
 */
public class Notification {
    String bookID;
    String description;
    Calendar date;

    /**
     * Constructor.
     * @param notificationString The notification text to be turned into a notification object
     */
    public Notification(String notificationString) {
        String[] parsedString = notificationString.split("\\|");
        this.bookID = parsedString[0];
        this.description = parsedString[1];
        //TODO: add date to notifications
        if (parsedString.length > 2){
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            try {
                cal.setTime(sdf.parse(parsedString[3]));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else {
            this.date = Calendar.getInstance();
        }
    }

    /**
     * Returns the description for the notification.
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the formatted date in YYYY-MM-DD.
     * @return The date.
     */
    public String getDate() {
        return this.date.get(Calendar.YEAR) + "-" +
                (this.date.get(Calendar.MONTH) + 1) + "-" +
                this.date.get(Calendar.DAY_OF_MONTH);
    }
}
