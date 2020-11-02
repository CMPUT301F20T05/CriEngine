package com.example.criengine;

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

    @Before
    public void init() {
        mock = new Profile();
    }

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
    }

    @Test
    public void testConstructorTwo() {
        Profile mock = new Profile("UserID", "newEmail@email.com", "UserName", "780-780-7800", "John", "Doe", "Bio");

        assertEquals(mock.getUserID(), "UserID");
        assertEquals(mock.getEmail(), "newEmail@email.com");
        assertEquals(mock.getUsername(), "UserName");
        assertEquals(mock.getPhone(), "780-780-7800");
        assertEquals(mock.getFirstname(), "John");
        assertEquals(mock.getLastname(), "Doe");
        assertEquals(mock.getBio(), "Bio");
        assertEquals(mock.getBooksOwned().size(), 0);
        assertEquals(mock.getBooksBorrowedOrRequested().size(), 0);
    }

    @Test
    public void testConstructorThree() {
        Profile mock = new Profile("UserID", "newEmail@email.com", "UserName", "780-780-7800", "John", "Doe");

        assertEquals(mock.getUserID(), "UserID");
        assertEquals(mock.getEmail(), "newEmail@email.com");
        assertEquals(mock.getUsername(), "UserName");
        assertEquals(mock.getPhone(), "780-780-7800");
        assertEquals(mock.getFirstname(), "John");
        assertEquals(mock.getLastname(), "Doe");
        assertNull(mock.getBio());
        assertEquals(mock.getBooksOwned().size(), 0);
        assertEquals(mock.getBooksBorrowedOrRequested().size(), 0);
    }

    @Test
    public void testUsernameMethods() {
        mock.setUsername("Nice");
        assertEquals(mock.getUsername(), "Nice");
    }

    @Test
    public void testPhoneMethods() {
        mock.setPhone("780-999-9199");
        assertEquals(mock.getPhone(), "780-999-9199");
    }

    @Test
    public void testFirstNameMethods() {
        mock.setFirstname("Captain");
        assertEquals(mock.getFirstname(), "Captain");
    }

    @Test
    public void testLastNameMethods() {
        mock.setLastname("Rex");
        assertEquals(mock.getLastname(), "Rex");
    }

    @Test
    public void testBioMethods() {
        mock.setBio("Rex");
        assertEquals(mock.getBio(), "Rex");
    }

    @Test
    public void testEmailMethods() {
        mock.setEmail("Rex@email.com");
        assertEquals(mock.getEmail(), "Rex@email.com");
    }

    @Test
    public void testUserIDMethods() {
        mock.setUserID("Rex");
        assertEquals(mock.getUserID(), "Rex");
    }

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
}