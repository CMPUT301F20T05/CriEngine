package com.example.criengine;

import com.example.criengine.Objects.Book;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

/**
 * Unit tests for the Book.java class.
 * These tests test every method in the class.
 */
public class BookUnitTest {
    Book mockBook;
    ArrayList<String> newRequesterList;

    @Before
    public void init() {
        newRequesterList = new ArrayList<>();
        mockBook = new Book("ID 1", "New Owner", "New Title", "New Author", "New Description", "New ISBN", "New Status", "New Borrower", newRequesterList, "New Geolocation", "New Image");
    }

    @Test
    public void testBookConstructorOne() {
        Book mockBook = new Book();

        assertNull(mockBook.getBookID());
        assertNull(mockBook.getOwner());
        assertNull(mockBook.getTitle());
        assertNull(mockBook.getAuthor());
        assertNull(mockBook.getDescription());
        assertNull(mockBook.getIsbn());
        assertNull(mockBook.getStatus());
        assertNull(mockBook.getBorrower());
        assertEquals(mockBook.getRequesters().size(), 0);
        assertNull(mockBook.getGeolocation());
        assertNull(mockBook.getImageURL());
    }

    @Test
    public void testBookConstructorTwo() {
        assertEquals(mockBook.getBookID(), "ID 1");
        assertEquals(mockBook.getOwner(), "New Owner");
        assertEquals(mockBook.getTitle(), "New Title");
        assertEquals(mockBook.getAuthor(), "New Author");
        assertEquals(mockBook.getDescription(), "New Description");
        assertEquals(mockBook.getIsbn(), "New ISBN");
        assertEquals(mockBook.getStatus(), "New Status");
        assertEquals(mockBook.getBorrower(), "New Borrower");
        assertEquals(mockBook.getRequesters().size(), 0);
        assertEquals(mockBook.getGeolocation(), "New Geolocation");
        assertEquals(mockBook.getImageURL(), "New Image");
    }

    @Test
    public void testBookConstructorThree() {
        Book mockBook = new Book("New Owner", "New Title", "New Author", "New Description", "New ISBN", "New Status", "New Borrower", newRequesterList, "New Geolocation", "New Image");

        assertNull(mockBook.getBookID());
        assertEquals(mockBook.getOwner(), "New Owner");
        assertEquals(mockBook.getTitle(), "New Title");
        assertEquals(mockBook.getAuthor(), "New Author");
        assertEquals(mockBook.getDescription(), "New Description");
        assertEquals(mockBook.getIsbn(), "New ISBN");
        assertEquals(mockBook.getStatus(), "New Status");
        assertEquals(mockBook.getBorrower(), "New Borrower");
        assertEquals(mockBook.getRequesters().size(), 0);
        assertEquals(mockBook.getGeolocation(), "New Geolocation");
        assertEquals(mockBook.getImageURL(), "New Image");
    }

    @Test
    public void testBookConstructorFour() {
        Book mockBook = new Book("New Owner", "New Title", "New Author", "New Description", "New ISBN", "New Status");

        assertNull(mockBook.getBookID());
        assertEquals(mockBook.getOwner(), "New Owner");
        assertEquals(mockBook.getTitle(), "New Title");
        assertEquals(mockBook.getAuthor(), "New Author");
        assertEquals(mockBook.getDescription(), "New Description");
        assertEquals(mockBook.getIsbn(), "New ISBN");
        assertEquals(mockBook.getStatus(), "New Status");
        assertNull(mockBook.getBorrower());
        assertEquals(mockBook.getRequesters().size(), 0);
        assertNull(mockBook.getGeolocation());
        assertNull(mockBook.getImageURL());
    }

    @Test
    public void testRequesterMethods() {
        mockBook.addRequesters("A new Requester");
        assertEquals(mockBook.getRequesters().size(), 1);
        assertEquals(mockBook.getRequesters().get(0), "A new Requester");

        mockBook.removeRequesters("A new Requester");
        assertEquals(mockBook.getRequesters().size(), 0);

        newRequesterList.add("Requester 1");
        newRequesterList.add("Requester 2");
        mockBook.setRequesters(newRequesterList);
        assertEquals(mockBook.getRequesters(), newRequesterList);
    }

    @Test
    public void testBorrowerMethods() {
        mockBook.setBorrower("A new Borrower");
        assertEquals(mockBook.getBorrower(), "A new Borrower");

        mockBook.removeBorrower();
        assertNull(mockBook.getBorrower());
    }

    @Test
    public void testConfirmationNeededMethods() {
        assertEquals(mockBook.isConfirmationNeeded(), true);
        mockBook.setConfirmationNeeded(true);
        assertEquals(mockBook.isConfirmationNeeded(), false);
    }

    @Test
    public void testOwnerMethods() {
        mockBook.setOwner("A new Owner");
        assertEquals(mockBook.getOwner(), "A new Owner");
    }

    @Test
    public void testTitleMethods() {
        mockBook.setTitle("A new Title");
        assertEquals(mockBook.getTitle(), "A new Title");
    }

    @Test
    public void testAuthorMethods() {
        mockBook.setAuthor("A new Author");
        assertEquals(mockBook.getAuthor(), "A new Author");
    }

    @Test
    public void testDescriptionMethods() {
        mockBook.setDescription("A new Description");
        assertEquals(mockBook.getDescription(), "A new Description");
    }

    @Test
    public void testISBNMethods() {
        mockBook.setIsbn("A new ISBN");
        assertEquals(mockBook.getIsbn(), "A new ISBN");
    }

    @Test
    public void testStatusMethods() {
        mockBook.setStatus("A new Status");
        assertEquals(mockBook.getStatus(), "A new Status");
    }

    @Test
    public void testLocationMethods() {
        mockBook.setGeolocation("A new Location");
        assertEquals(mockBook.getGeolocation(), "A new Location");
    }

    @Test
    public void testImageMethods() {
        mockBook.setImageURL("A new Image");
        assertEquals(mockBook.getImageURL(), "A new Image");
    }
}