package com.example.criengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Book {
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

    // Needed for firebase object conversion
    public Book() {
        this.requesters = new ArrayList<>();

    }

    // BOOKS WITH A BOOK ID WILL BE SAVED UNDER THAT ID. BOOKS WITHOUT AN ID WILL BE ASSIGNED A RANDOM ID
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

//    public Book(Map<String, Object> data) {
//        this.bookID = (String) data.get("bookID");
//        this.owner = (String) data.get("owner");
//        this.title = (String) data.get("title");
//        this.author = (String) data.get("author");
//        this.description = (String) data.get("description");
//        this.isbn = (String) data.get("isbn");
//        this.status = (String) data.get("status");
//
//        if (data.get("borrower") != null) {
//            this.borrower = (String) data.get("borrower");
//        }
//        if (data.get("geolocation") != null) {
//            this.geolocation = (String) data.get("geolocation");
//        }
//        if (data.get("imageURL") != null) {
//            this.imageURL = (String) data.get("imageURL");
//        }
//
//        if (((ArrayList<String>) data.get("requesters")).isEmpty()) {
//            this.requesters = new ArrayList<>();
//        } else {
//            this.requesters = (ArrayList<String>) data.get("requesters");
//        }
//    }

    public String getBookID() {
        return bookID;
    }

    public String getOwner() {
        return owner;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getStatus() {
        return status;
    }

    public String getBorrower() {
        return borrower;
    }

    public List<String> getRequesters() {
        return requesters;
    }

    public String getGeolocation() {
        return geolocation;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public void setRequesters(ArrayList<String> requesters) {
        this.requesters = requesters;
    }

    public void setGeolocation(String geolocation) {
        this.geolocation = geolocation;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
