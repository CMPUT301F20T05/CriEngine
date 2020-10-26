package com.example.criengine;

import java.util.Map;

public class Profile {

    private String username;
    private String phone;
    private String firstname;
    private String lastname;
    private String email;
    private String bio;

    public Profile(String username, String phone, String firstname, String lastname, String email) {
        this.username = username;
        this.phone = phone;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    public Profile() {

    }

    public Profile(Map<String, Object> map) {
        this.username = (String) map.get("username");
        this.phone = (String) map.get("phone");
        this.firstname = (String) map.get("firstname");
        this.lastname = (String) map.get("lastname");
        this.email = (String) map.get("email");
        this.bio = (String) map.get("bio");

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

}
