package com.example.criengine;

import java.util.ArrayList;

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


    public Profile() {
        this.booksOwned = new ArrayList<>();
        this.booksBorrowedOrRequested = new ArrayList<>();
    }

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
    }

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
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public ArrayList<String> getBooksOwned() {
        return booksOwned;
    }

    public void setBooksOwned(ArrayList<String> booksOwned) {
        this.booksOwned = booksOwned;
    }

    public ArrayList<String> getBooksBorrowedOrRequested() {
        return booksBorrowedOrRequested;
    }

    public void setBooksBorrowedOrRequested(ArrayList<String> booksBorrowedOrRequested) {
        this.booksBorrowedOrRequested = booksBorrowedOrRequested;
    }
}
