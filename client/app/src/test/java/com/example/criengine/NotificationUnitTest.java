package com.example.criengine;

import com.example.criengine.Objects.Notification;
import org.junit.Before;
import org.junit.Test;
import java.util.Calendar;
import static org.junit.Assert.*;

/**
 * Unit tests for the Notification.java class.
 */
public class NotificationUnitTest {
    Notification notification;

    @Before
    public void init() {
        notification = new Notification("This is a mock notification");
    }

    @Test
    public void testGetterMethods() {
        assertEquals(notification.getDescription(), "This is a mock notification");

        Calendar date = Calendar.getInstance();
        String formattedDate = date.get(Calendar.YEAR) + "-" +
                (date.get(Calendar.MONTH) + 1) + "-" +
                date.get(Calendar.DAY_OF_MONTH);

        assertEquals(notification.getDate(), formattedDate);
    }
}