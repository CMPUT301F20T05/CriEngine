package com.example.criengine.Objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates a book object.
 */
public class Book implements Serializable {
    private String bookID;
    private String owner;
    private String title;
    private String author;
    private String description;
    private String isbn;
    private String status;
    private String borrower;
    private ArrayList<String> requesters;
    private String geolocation;
    private String imageURL;
    private PotentialBorrower potential;

    /**
     * Constructor 1.
     * Needed for firebase object.
     */
    public Book() {
        this.requesters = new ArrayList<>();
    }

    /**
     * Constructor 2.
     * BOOKS WITH A BOOK ID WILL BE SAVED UNDER THAT ID. BOOKS WITHOUT AN ID WILL BE ASSIGNED A RANDOM ID.
     * @param bookID The book ID
     * @param owner The owner of the book.
     * @param title The title of the book.
     * @param author The author of the book.
     * @param description The description of the book.
     * @param isbn The isbn code of the book.
     * @param status The status of the book.
     * @param borrower The borrower of the book.
     * @param requesters A list of people requesting the book.
     * @param geolocation The current location of the book.
     * @param imageURL The image URL.
     */
    public Book(String bookID, String owner, String title, String author, String description, String isbn, String status, String borrower, List<String> requesters, String geolocation, String imageURL) {
        this.bookID = bookID;
        this.owner = owner;
        this.title = title;
        this.author = author;
        this.description = description;
        this.isbn = isbn;
        this.status = status;
        this.borrower = borrower;
        this.requesters = (ArrayList<String>) requesters;
        this.geolocation = geolocation;
        this.imageURL = imageURL;
    }

    /**
     * Constructor 3.
     * @param owner The owner of the book.
     * @param title The title of the book.
     * @param author The author of the book.
     * @param description The description of the book.
     * @param isbn The isbn code of the book.
     * @param status The status of the book.
     * @param borrower The borrower of the book.
     * @param requesters A list of people requesting the book.
     * @param geolocation The current location of the book.
     * @param imageURL The image URL.
     */
    public Book(String owner, String title, String author, String description, String isbn, String status, String borrower, List<String> requesters, String geolocation, String imageURL) {
        this.bookID = null;
        this.owner = owner;
        this.title = title;
        this.author = author;
        this.description = description;
        this.isbn = isbn;
        this.status = status;
        this.borrower = borrower;
        this.requesters = (ArrayList<String>) requesters;
        this.geolocation = geolocation;
        this.imageURL = imageURL;
    }

    /**
     * Constructor 4.
     * @param owner The owner of the book.
     * @param title The title of the book.
     * @param author The author of the book.
     * @param description The description of the book.
     * @param isbn The isbn code of the book.
     * @param status The status of the book.
     */
    public Book(String owner, String title, String author, String description, String isbn, String status) {
        this.bookID = null;
        this.owner = owner;
        this.title = title;
        this.author = author;
        this.description = description;
        this.isbn = isbn;
        this.status = status;
        this.borrower = null;
        this.requesters = new ArrayList<>();
        this.geolocation = null;
        this.imageURL = null;
    }

    /**
     * Get the book id.
     * @return The book id.
     */
    public String getBookID() {
        return bookID;
    }

    /**
     * Get the owner of the book.
     * @return The owner of the book.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Get the title of the book.
     * @return The title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the author of the book.
     * @return The author.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Get the description of the book.
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the ISBN.
     * @return The ISBN.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Get the status of the book.
     * @return The status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Get the current borrower of the book.
     * @return The borrower.
     */
    public String getBorrower() {
        return borrower;
    }

    /**
     * Get the list of people requesting a book.
     * @return The list of requesters.
     */
    public List<String> getRequesters() {
        return requesters;
    }

    /**
     * Add a person to list of requesters.
     * @param name The name of the person requesting the book.
     */
    public void addRequesters(String name) {
        requesters.add(name);
    }

    /**
     * Remove a requester from the list.
     * @param name The name of the person to be removed.
     */
    public void removeRequesters(String name) {
        requesters.remove(name);
    }

    /**
     * Wipe the current borrower.
     */
    public void removeBorrower() {
        borrower = null;
    }

    /**
     * Get the location of the book.
     * @return The geo location.
     */
    public String getGeolocation() {
        return geolocation;
    }

    /**
     * Get a potential borrower.
     * This is someone who requested a book + was accepted, but the hand-off has yet to occur.
     * @return The potential borrower.
     */
    public PotentialBorrower getPotentialBorrower() {
        return potential;
    }

    /**
     * Set a potential borrower.
     * This is someone who requested a book + was accepted, but the hand-off has yet to occur.
     * @param name The name of the person to be added.
     */
    public void setPotentialBorrower(String name) {
        potential = new PotentialBorrower(name);
    }

    /**
     * Wipe the potential borrower.
     */
    public void removePotentialBorrower() {
        potential = null;
    }

    /**
     * Returns the image URL.
     * @return The image URL.
     */
    public String getImageURL() {
        return imageURL;
    }

    /**
     * Set the new book ID.
     * @param bookID The new book id.
     */
    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    /**
     * Set the new owner of the book.
     * @param owner The name of the new owner.
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Set the title of the book.
     * @param title The new title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Set the new author of the book.
     * @param author The name of the author.
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Set a new description for the book.
     * @param description The new description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Set the new ISBN.`
     * @param isbn The new ISBN.
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Change the status of the book.
     * @param status The new status.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Set a borrower for the book.
     * @param borrower The name of the person borrowing the book.
     */
    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    /**
     * Set a new list of requesters for the book.
     * @param requesters The new list of requesters.
     */
    public void setRequesters(ArrayList<String> requesters) {
        this.requesters = requesters;
    }

    /**
     * The new location of the book.
     * @param geolocation The location.
     */
    public void setGeolocation(String geolocation) {
        this.geolocation = geolocation;
    }

    /**
     * Sets a new image url for thebook.
     * @param imageURL The new image URL.
     */
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
