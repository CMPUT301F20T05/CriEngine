package com.example.criengine.Objects;

import java.util.ArrayList;

/**
 * Creates a profile for the user.
 */
public class Profile {
    private String userID;
    private String email;
    private String username;
    private String phone;
    private String firstname;
    private String lastname;
    private String bio;
    private ArrayList<String> booksOwned;
    private ArrayList<String> booksBorrowedOrRequested;
    private ArrayList<Notification> notifications;

    /**
     * Constructor 1. Takes no inputs and instantiates the different arrays.
     */
    public Profile() {
        this.booksOwned = new ArrayList<>();
        this.booksBorrowedOrRequested = new ArrayList<>();
        this.notifications = new ArrayList<>();
    }

    /**
     * Constructor 2.
     * Instantiates a profile with a bio.
     * @param userID The user identifier.
     * @param email The email of the user.
     * @param username The username of the profile.
     * @param phone The phone number for the user.
     * @param firstname The first name of the user.
     * @param lastname The last name of the user.
     * @param bio The bio that the user wanted to enter.
     */
    public Profile(String userID, String email, String username, String phone, String firstname, String lastname, String bio) {
        this.userID = userID;
        this.email = email;
        this.username = username;
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
     * @param userID The user identifier.
     * @param email The email of the user.
     * @param username The username of the profile.
     * @param phone The phone number for the user.
     * @param firstname The first name of the user.
     * @param lastname The last name of the user.
     */
    public Profile(String userID, String email, String username, String phone, String firstname, String lastname) {
        this.userID = userID;
        this.email = email;
        this.username = username;
        this.phone = phone;
        this.firstname = firstname;
        this.lastname = lastname;
        this.bio = null;
        this.booksOwned = new ArrayList<>();
        this.booksBorrowedOrRequested = new ArrayList<>();
        this.notifications = new ArrayList<>();
    }

    /**
     * Get the username for the profile.
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username for the profile.
     * @param username The new username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the phone number related to the account.
     * @return The phone number.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Set a new phone number for the account.
     * @param phone The new phone number.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Get the first name of the user.
     * @return The first name.
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Change the first name for the user.
     * @param firstname The new first name.
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * Get the last name of the user.
     * @return The last name.
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Set a new last name for the user.
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
     * Set a new email for the account.
     * @param email The new email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the user identifier.
     * @return The ID.
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Set a new user identifier for the profile.
     * @param userID The new user ID.
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * Get a list of owned books.
     * @return The list of owned books.
     */
    public ArrayList<String> getBooksOwned() {
        return booksOwned;
    }

    /**
     * Set a new list of owned books.
     * @param booksOwned The list of owned books.
     */
    public void setBooksOwned(ArrayList<String> booksOwned) {
        this.booksOwned = booksOwned;
    }

    /**
     * Get a list of books that the user has borrowed or requested.
     * @return The list of borrowed or requested books.
     */
    public ArrayList<String> getBooksBorrowedOrRequested() {
        return booksBorrowedOrRequested;
    }

    /**
     * Set a list of borrowed or requested books.
     * @param booksBorrowedOrRequested The list of borrowed or requested books.
     */
    public void setBooksBorrowedOrRequested(ArrayList<String> booksBorrowedOrRequested) {
        this.booksBorrowedOrRequested = booksBorrowedOrRequested;
    }

    /**
     * Add a notification for the user.
     * @param notif The notification to be added.
     */
    public void addNotification(Notification notif) {
        notifications.add(notif);
    }

    /**
     * Remove a notification from the user.
     * @param notif The notification to be removed.
     */
    public void removeNotification(Notification notif) {
        notifications.remove(notif);
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<Notification> newList) {
        notifications = newList;
    }
}
