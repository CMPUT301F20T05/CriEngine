package com.example.criengine.Objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Creates a user profile.
 * Contains all information specific to a profile.
 */
public class Profile implements Serializable {
    private String userID;
    private String email;
    private String username;
    private String address;
    private String phone;
    private String firstname;
    private String lastname;
    private String bio;
    private ArrayList<String> booksOwned;
    private ArrayList<String> booksBorrowedOrRequested;
    private ArrayList<String> notifications;

    /**
     * Constructor 1. No inputs, simply instantiates the arraylists.
     */
    public Profile() {
        this.booksOwned = new ArrayList<>();
        this.booksBorrowedOrRequested = new ArrayList<>();
        this.notifications = new ArrayList<>();
    }

    /**
     * Constructor 2.
     * @param userID The user ID.
     * @param email The email of the user.
     * @param username The username for the user.
     * @param phone The phone number for the user.
     * @param firstname The firstname of the user.
     * @param lastname The lastname of the user.
     * @param bio The bio for the user.
     */
    public Profile(String userID, String email, String username, String address, String phone, String firstname, String lastname, String bio) {
        this.userID = userID;
        this.email = email;
        this.username = username;
        this.address = address;
        this.phone = phone;
        this.firstname = firstname;
        this.lastname = lastname;
        this.bio = bio;
        this.booksOwned = new ArrayList<>();
        this.booksBorrowedOrRequested = new ArrayList<>();
        this.notifications = new ArrayList<>();
    }

    /**
     * Constructor 3.
     * @param userID The user ID.
     * @param email The email of the user.
     * @param username The username of the user.
     * @param firstname The first name of the user.
     * @param lastname The last name of the user.
     */
    public Profile(String userID, String email, String username, String firstname, String lastname) {
        this.userID = userID;
        this.email = email;
        this.username = username;
        this.address = null;
        this.phone = null;
        this.firstname = firstname;
        this.lastname = lastname;
        this.bio = null;
        this.booksOwned = new ArrayList<>();
        this.booksBorrowedOrRequested = new ArrayList<>();
        this.notifications = new ArrayList<>();
    }

    /**
     * Get the username of the profile.
     * @return The username for the profile.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username for the profile.
     * @param username The new username for the profile.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the address of the profile.
     * @return The address for the profile.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set the address for the profile.
     * @param address The new address for the profile.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Get the phone number from the profile.
     * @return The phone number.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Set a new phone number for the profile
     * @param phone The new phone number.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Get the first name from the profile.
     * @return The first name.
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Set a new first name for the profile.
     * @param firstname The new first name.
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * Get the last name for the profile.
     * @return The last name.
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Set a new last name for the profile.
     * @param lastname The new last name.
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * Get the bio for the profile.
     * @return The bio.
     */
    public String getBio() {
        return bio;
    }

    /**
     * Set a new bio for the profile.
     * @param bio The new bio.
     */
    public void setBio(String bio) {
        this.bio = bio;
    }

    /**
     * Get the email for the profile.
     * @return The email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set a new email for the profile
     * @param email The new email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the user ID from the profile.
     * @return The user ID.
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Set a new user Id for the profile.
     * @param userID The new user ID.
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * Get list of owned books from the profile.
     * @return The list of owned books.
     */
    public ArrayList<String> getBooksOwned() {
        return booksOwned;
    }

    /**
     * Set a new list of owned books.
     * @param booksOwned The new list of owned books.
     */
    public void setBooksOwned(ArrayList<String> booksOwned) {
        this.booksOwned = booksOwned;
    }

    /**
     * Get the list of borrowed or requested books.
     * @return The list of borrowed or requested books.
     */
    public ArrayList<String> getBooksBorrowedOrRequested() {
        return booksBorrowedOrRequested;
    }

    /**
     * Add a book to the list of owned books.
     * @param bookID The ID of the book.
     */
    public void addBooksOwned(String bookID) {
        this.booksOwned.add(bookID);
    }

    /**
     * Remove a book from the list of owned books.
     * @param bookID The ID of the book.
     */
    public void removeBooksOwned(String bookID) {
        this.booksOwned.remove(bookID);
    }

    /**
     * Set a new list of borrowed or requested books.
     * @param booksBorrowedOrRequested The new list of borrowed or requested books.
     */
    public void setBooksBorrowedOrRequested(ArrayList<String> booksBorrowedOrRequested) {
        this.booksBorrowedOrRequested = booksBorrowedOrRequested;
    }

    /**
     * Add a book to the list of borrowed or requested books.
     * @param bookID The ID of the book.
     */
    public void addBooksBorrowedOrRequested(String bookID) {
        this.booksBorrowedOrRequested.add(bookID);
    }

    /**
     * Remove a book from the list of borrowed of requested books.
     * @param bookID The ID of the book.
     */
    public void removeBooksBorrowedOrRequested(String bookID) {
        this.booksBorrowedOrRequested.remove(bookID);
    }

    /**
     * Add a notification for the user.
     * @param notif The notification to be added.
     */
    public void addNotification(String notif) {
        notifications.add(notif);
    }

    /**
     * Remove a notification from the list.
     * @param notif The notification to remove.
     */
    public void removeNotification(String notif) {
        notifications.remove(notif);
    }

    /**
     * Get the list of notifications.
     * @return The list of notifications.
     */
    public ArrayList<String> getNotifications() {
        return notifications;
    }

    /**
     * Set a new list of notifications.
     * @param newList The notification list to be set.
     */
    public void setNotifications(ArrayList<String> newList) {
        notifications = newList;
    }
}
