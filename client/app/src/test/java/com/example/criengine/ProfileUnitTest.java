package com.example.criengine;

import com.example.criengine.Objects.Notification;
import com.example.criengine.Objects.Profile;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

/**
 * Unit tests for the Profile.java class.
 * Tests every method + constructor in the file.
 */
public class ProfileUnitTest {
    Profile mock;

    /**
     * Run before every test case.
     */
    @Before
    public void init() {
        mock = new Profile();
    }

    /**
     * Test the first constructor.
     */
    @Test
    public void testConstructorOne() {
        assertNull(mock.getUserID());
        assertNull(mock.getEmail());
        assertNull(mock.getUsername());
        assertNull(mock.getPhone());
        assertNull(mock.getFirstname());
        assertNull(mock.getLastname());
        assertNull(mock.getBio());
        assertEquals(mock.getBooksOwned().size(), 0);
        assertEquals(mock.getBooksBorrowedOrRequested().size(), 0);
        assertEquals(mock.getNotifications().size(), 0);
    }

    /**
     * Test the second constructor.
     */
    @Test
    public void testConstructorTwo() {
        Profile mock = new Profile("UserID", "newEmail@email.com", "UserName", "address", "780-780-7800", "John", "Doe", "Bio");

        assertEquals(mock.getUserID(), "UserID");
        assertEquals(mock.getEmail(), "newEmail@email.com");
        assertEquals(mock.getUsername(), "UserName");
        assertEquals(mock.getAddress(), "address");
        assertEquals(mock.getPhone(), "780-780-7800");
        assertEquals(mock.getFirstname(), "John");
        assertEquals(mock.getLastname(), "Doe");
        assertEquals(mock.getBio(), "Bio");
        assertEquals(mock.getBooksOwned().size(), 0);
        assertEquals(mock.getBooksBorrowedOrRequested().size(), 0);
        assertEquals(mock.getNotifications().size(), 0);
    }

    /**
     * Test the third constructor.
     */
    @Test
    public void testConstructorThree() {
        Profile mock = new Profile("UserID", "newEmail@email.com", "UserName", "John", "Doe");

        assertEquals(mock.getUserID(), "UserID");
        assertEquals(mock.getEmail(), "newEmail@email.com");
        assertEquals(mock.getUsername(), "UserName");
        assertEquals(mock.getFirstname(), "John");
        assertEquals(mock.getLastname(), "Doe");
        assertNull(mock.getAddress());
        assertNull(mock.getPhone());
        assertNull(mock.getBio());
        assertEquals(mock.getBooksOwned().size(), 0);
        assertEquals(mock.getBooksBorrowedOrRequested().size(), 0);
        assertEquals(mock.getNotifications().size(), 0);
    }

    /**
     * Test getter/setters for the username attribute.
     */
    @Test
    public void testUsernameMethods() {
        mock.setUsername("Nice");
        assertEquals(mock.getUsername(), "Nice");
    }

    /**
     * Test getter/setters for the phone attribute.
     */
    @Test
    public void testPhoneMethods() {
        mock.setPhone("780-999-9199");
        assertEquals(mock.getPhone(), "780-999-9199");
    }

    /**
     * Test getter/setters for the first-name attribute.
     */
    @Test
    public void testFirstNameMethods() {
        mock.setFirstname("Captain");
        assertEquals(mock.getFirstname(), "Captain");
    }

    /**
     * Test getter/setters for the last-name attribute.
     */
    @Test
    public void testLastNameMethods() {
        mock.setLastname("Rex");
        assertEquals(mock.getLastname(), "Rex");
    }

    /**
     * Test getter/setters for the bio attribute.
     */
    @Test
    public void testBioMethods() {
        mock.setBio("Rex");
        assertEquals(mock.getBio(), "Rex");
    }

    /**
     * Test getter/setters for the email attribute.
     */
    @Test
    public void testEmailMethods() {
        mock.setEmail("Rex@email.com");
        assertEquals(mock.getEmail(), "Rex@email.com");
    }

    /**
     * Test getter/setters for the userID attribute.
     */
    @Test
    public void testUserIDMethods() {
        mock.setUserID("Rex");
        assertEquals(mock.getUserID(), "Rex");
    }

    /**
     * Test getter/setters/add/remove for the ownedBooks attribute.
     */
    @Test
    public void testOwnedBooksMethods() {
        mock.addBooksOwned("This is my book");
        assertEquals(mock.getBooksOwned().get(0), "This is my book");

        mock.removeBooksOwned("This is my book");
        assertEquals(mock.getBooksOwned().size(), 0);

        ArrayList<String> newOwnedBooks = new ArrayList<>();
        newOwnedBooks.add("A new book!");
        mock.setBooksOwned(newOwnedBooks);
        assertEquals(mock.getBooksOwned().get(0), "A new book!");
    }

    /**
     * Test getter/setters/add/remove for the borrowedOrRequested attribute.
     */
    @Test
    public void testBorrowedOrRequestedMethods() {
        mock.addBooksBorrowedOrRequested("A new borrowed book");
        assertEquals(mock.getBooksBorrowedOrRequested().get(0), "A new borrowed book");

        mock.removeBooksBorrowedOrRequested("A new borrowed book");
        assertEquals(mock.getBooksBorrowedOrRequested().size(), 0);

        ArrayList<String> newBorrowedOrRequested = new ArrayList<>();
        newBorrowedOrRequested.add("A new book!");
        mock.setBooksBorrowedOrRequested(newBorrowedOrRequested);
        assertEquals(mock.getBooksBorrowedOrRequested().get(0), "A new book!");
    }

    /**
     * Test getter/setters for wishlist attribute.
     */
    @Test
    public void testWishlistMethods() {
        ArrayList<String> mockList = new ArrayList<>();
        mockList.add("Book1");
        mock.setWishlist(mockList);
        assertEquals(mock.getWishlist().size(), 1);
    }

    /**
     * Test getter/setters/add/remove for the notification attribute.
     */
    @Test
    public void testNotificationMethods() {
        Notification mockNotification = new Notification("Test|Mock notification.");
        mock.addNotification(mockNotification.getDescription());
        assertEquals(mock.getNotifications().get(0), "Mock notification.");

        mock.removeNotification(mockNotification.getDescription());
        assertEquals(mock.getNotifications().size(), 0);

        ArrayList<String> newMockNotificationList = new ArrayList<>();
        Notification anotherMockNotification = new Notification("Test|Another mock notification.");
        newMockNotificationList.add(anotherMockNotification.getDescription());
        mock.setNotifications(newMockNotificationList);
        assertEquals(mock.getNotifications().get(0), "Another mock notification.");
    }
}