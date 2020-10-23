package com.example.criengine;

import java.util.Map;

public class Profile {

    private String username;
    private long phone;
    private String firstname;
    private String lastname;
    private String email;

    public Profile(String username, long phone, String firstname, String lastname, String email) {
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
        this.phone = (long) map.get("phone");
        this.firstname = (String) map.get("firstname");
        this.lastname = (String) map.get("lastname");
        this.email = (String) map.get("email");

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
