package com.example.criengine.Objects;

import java.util.Calendar;

/**
 * Notification class. Creates a notification with a description and a date-of-creation.
 */
public class Notification {
    String description;
    Calendar date;

    /**
     * Constructor.
     * @param description The description to be displayed.
     */
    public Notification(String description) {
        this.description = description;
        this.date = Calendar.getInstance();
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
