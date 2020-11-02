package com.example.criengine;

import com.example.criengine.Objects.Book;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class BookUnitTest {
    @Test
    public void testBookConstructorOne() {
        Book newBook = new Book();

        assertEquals(newBook.getRequesters().size(), 0);
    }

    @Test
    public void testBookConstructorTwo() {
        List<String> newRequesterList = new ArrayList<>();
        Book newBook = new Book("ID 1", "New Owner", "New Title", "New Author", "New Description", "New ISBN", "New Status", "New Borrower", newRequesterList, "New Geolocation", "New Image");

        assertEquals(newBook.getBookID(), "ID 1");
        assertEquals(newBook.getOwner(), "New Owner");
        assertEquals(newBook.getTitle(), "New Title");
        assertEquals(newBook.getAuthor(), "New Author");
        assertEquals(newBook.getDescription(), "New Description");
        assertEquals(newBook.getIsbn(), "New ISBN");
        assertEquals(newBook.getStatus(), "New Status");
        assertEquals(newBook.getBorrower(), "New Borrower");
        assertEquals(newBook.getRequesters().size(), 0);
        assertEquals(newBook.getGeolocation(), "New Geolocation");
        assertEquals(newBook.getImageURL(), "New Image");
    }

    @Test
    public void testBookConstructorThree() {
        List<String> newRequesterList = new ArrayList<>();
        Book newBook = new Book("New Owner", "New Title", "New Author", "New Description", "New ISBN", "New Status", "New Borrower", newRequesterList, "New Geolocation", "New Image");

        assertNull(newBook.getBookID());
        assertEquals(newBook.getOwner(), "New Owner");
        assertEquals(newBook.getTitle(), "New Title");
        assertEquals(newBook.getAuthor(), "New Author");
        assertEquals(newBook.getDescription(), "New Description");
        assertEquals(newBook.getIsbn(), "New ISBN");
        assertEquals(newBook.getStatus(), "New Status");
        assertEquals(newBook.getBorrower(), "New Borrower");
        assertEquals(newBook.getRequesters().size(), 0);
        assertEquals(newBook.getGeolocation(), "New Geolocation");
        assertEquals(newBook.getImageURL(), "New Image");
    }

    @Test
    public void testBookConstructorFour() {
        List<String> newRequesterList = new ArrayList<>();
        Book newBook = new Book("New Owner", "New Title", "New Author", "New Description", "New ISBN", "New Status");

        assertNull(newBook.getBookID());
        assertEquals(newBook.getOwner(), "New Owner");
        assertEquals(newBook.getTitle(), "New Title");
        assertEquals(newBook.getAuthor(), "New Author");
        assertEquals(newBook.getDescription(), "New Description");
        assertEquals(newBook.getIsbn(), "New ISBN");
        assertEquals(newBook.getStatus(), "New Status");
        assertNull(newBook.getBorrower());
        assertEquals(newBook.getRequesters().size(), 0);
        assertNull(newBook.getGeolocation());
        assertNull(newBook.getImageURL());
    }

    @Test
    public void testRequesterMethods() {
        ArrayList<String> newRequesterList = new ArrayList<>();
        Book newBook = new Book("ID 1", "New Owner", "New Title", "New Author", "New Description", "New ISBN", "New Status", "New Borrower", newRequesterList, "New Geolocation", "New Image");

        newBook.addRequesters("A new Requester");
        assertEquals(newBook.getRequesters().size(), 1);
        assertEquals(newBook.getRequesters().get(0), "A new Requester");

        newBook.removeRequesters("A new Requester");
        assertEquals(newBook.getRequesters().size(), 0);

        newRequesterList.add("Requester 1");
        newRequesterList.add("Requester 2");
        newBook.setRequesters(newRequesterList);
        assertEquals(newBook.getRequesters(), newRequesterList);
    }

    @Test
    public void testBorrowerMethods() {
        List<String> newRequesterList = new ArrayList<>();
        Book newBook = new Book("ID 1", "New Owner", "New Title", "New Author", "New Description", "New ISBN", "New Status", "New Borrower", newRequesterList, "New Geolocation", "New Image");

        newBook.setBorrower("A new Borrower");
        assertEquals(newBook.getBorrower(), "A new Borrower");

        newBook.removeBorrower();
        assertNull(newBook.getBorrower());
    }

    @Test
    public void testOwnerMethods() {
        List<String> newRequesterList = new ArrayList<>();
        Book newBook = new Book("ID 1", "New Owner", "New Title", "New Author", "New Description", "New ISBN", "New Status", "New Borrower", newRequesterList, "New Geolocation", "New Image");

        newBook.setOwner("A new Owner");
        assertEquals(newBook.getOwner(), "A new Owner");
    }

    @Test
    public void testTitleMethods() {
        List<String> newRequesterList = new ArrayList<>();
        Book newBook = new Book("ID 1", "New Owner", "New Title", "New Author", "New Description", "New ISBN", "New Status", "New Borrower", newRequesterList, "New Geolocation", "New Image");

        newBook.setTitle("A new Title");
        assertEquals(newBook.getTitle(), "A new Title");
    }

    @Test
    public void testAuthorMethods() {
        List<String> newRequesterList = new ArrayList<>();
        Book newBook = new Book("ID 1", "New Owner", "New Title", "New Author", "New Description", "New ISBN", "New Status", "New Borrower", newRequesterList, "New Geolocation", "New Image");

        newBook.setAuthor("A new Author");
        assertEquals(newBook.getAuthor(), "A new Author");
    }

    @Test
    public void testDescriptionMethods() {
        List<String> newRequesterList = new ArrayList<>();
        Book newBook = new Book("ID 1", "New Owner", "New Title", "New Author", "New Description", "New ISBN", "New Status", "New Borrower", newRequesterList, "New Geolocation", "New Image");

        newBook.setDescription("A new Description");
        assertEquals(newBook.getDescription(), "A new Description");
    }

    @Test
    public void testISBNMethods() {
        List<String> newRequesterList = new ArrayList<>();
        Book newBook = new Book("ID 1", "New Owner", "New Title", "New Author", "New Description", "New ISBN", "New Status", "New Borrower", newRequesterList, "New Geolocation", "New Image");

        newBook.setIsbn("A new ISBN");
        assertEquals(newBook.getIsbn(), "A new ISBN");
    }

    @Test
    public void testStatusMethods() {
        List<String> newRequesterList = new ArrayList<>();
        Book newBook = new Book("ID 1", "New Owner", "New Title", "New Author", "New Description", "New ISBN", "New Status", "New Borrower", newRequesterList, "New Geolocation", "New Image");

        newBook.setStatus("A new Status");
        assertEquals(newBook.getStatus(), "A new Status");
    }

    @Test
    public void testLocationMethods() {
        List<String> newRequesterList = new ArrayList<>();
        Book newBook = new Book("ID 1", "New Owner", "New Title", "New Author", "New Description", "New ISBN", "New Status", "New Borrower", newRequesterList, "New Geolocation", "New Image");

        newBook.setGeolocation("A new Location");
        assertEquals(newBook.getGeolocation(), "A new Location");
    }

    @Test
    public void testImageMethods() {
        List<String> newRequesterList = new ArrayList<>();
        Book newBook = new Book("ID 1", "New Owner", "New Title", "New Author", "New Description", "New ISBN", "New Status", "New Borrower", newRequesterList, "New Geolocation", "New Image");

        newBook.setImageURL("A new Image");
        assertEquals(newBook.getImageURL(), "A new Image");
    }
}