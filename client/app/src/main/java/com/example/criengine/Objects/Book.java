package com.example.criengine.Objects;

import com.google.firebase.firestore.Exclude;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Creates a book object.
 */
public class Book implements Serializable {
    private String bookID;
    private String owner;
    private String ownerUsername;
    private String title;
    private String author;
    private String description;
    private String isbn;
    private String status;
    private String borrower;
    private boolean confirmationNeeded;
    private ArrayList<String> requesters;
    private String geolocation;
    private String imageURL;

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
     * @param owner The owner of the book's ID.
     * @param ownerUsername The owner of the book's username.
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
    public Book(String bookID, String owner, String ownerUsername, String title,  String author, String description, String isbn, String status, String borrower, List<String> requesters, String geolocation, String imageURL) {
        this.bookID = bookID;
        this.owner = owner;
        this.title = title;
        this.ownerUsername = ownerUsername;
        this.author = author;
        this.description = description;
        this.isbn = isbn;
        this.status = status;
        this.borrower = borrower;
        this.confirmationNeeded = false;
        this.requesters = (ArrayList<String>) requesters;
        this.geolocation = geolocation;
        this.imageURL = imageURL;
    }

    /**
     * Constructor 3.
     * @param owner The owner of the book's ID.
     * @param ownerUsername The owner of the book's username.
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
    public Book(String owner, String ownerUsername, String title, String author, String description, String isbn, String status, String borrower, List<String> requesters, String geolocation, String imageURL) {
        this.bookID = null;
        this.owner = owner;
        this.ownerUsername = ownerUsername;
        this.title = title;
        this.author = author;
        this.description = description;
        this.isbn = isbn;
        this.status = status;
        this.borrower = borrower;
        this.confirmationNeeded = false;
        this.requesters = (ArrayList<String>) requesters;
        this.geolocation = geolocation;
        this.imageURL = imageURL;
    }

    /**
     * Constructor 4.
     * @param owner The owner of the book's ID.
     * @param ownerUsername The owner of the book's username.
     * @param title The title of the book.
     * @param author The author of the book.
     * @param description The description of the book.
     * @param isbn The isbn code of the book.
     * @param status The status of the book.
     */
    public Book(String owner, String ownerUsername, String title, String author, String description, String isbn, String status) {
        this.bookID = null;
        this.owner = owner;
        this.ownerUsername = ownerUsername;
        this.title = title;
        this.author = author;
        this.description = description;
        this.isbn = isbn;
        this.status = status;
        this.borrower = null;
        this.confirmationNeeded = false;
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
     * Get the ID of the book's owner.
     * @return The ID of the book's owner.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Get the username of the book's owner.
     * @return The username of the book's owner.
     */
    public String getOwnerUsername() {
        return ownerUsername;
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
     * Get whether the current transaction the book is in needs confirmation.
     * @return true if the action needs to be confirmed
     */
    public boolean isConfirmationNeeded() {
        return confirmationNeeded;
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
     * @param userID The userID of the person requesting the book.
     */
    public void addRequesters(String userID) {
        requesters.add(userID);
    }

    /**
     * Remove a requester from the list.
     * @param userID The userID of the person to be removed.
     */
    public void removeRequesters(String userID) {
        requesters.remove(userID);
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
     * Get the location of the book.
     * @return The geo location.
     */
    @Exclude
    public LatLng getLatLng() {
        if (geolocation == null) {
            return null;
        }

        String regex = "LatLng \\[latitude=(.+?), longitude=(.+?), altitude=0\\.0\\]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(geolocation);
        matcher.matches();
        return new LatLng(Double.parseDouble(matcher.group(1)),Double.parseDouble(matcher.group(2)));
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
     * @param owner The userID of the new owner.
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Set the username of the book's owner
     * @param ownerUsername The username of the new owner.
     */
    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
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
     * Set the new ISBN.
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
     * @param borrower The userID of the person borrowing the book.
     */
    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    /**
     * Set whether the current transaction has been confirmed
     * @param confirmationNeeded whether the book needs confirmation
     */
    public void setConfirmationNeeded(boolean confirmationNeeded) {
        this.confirmationNeeded = confirmationNeeded;
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
