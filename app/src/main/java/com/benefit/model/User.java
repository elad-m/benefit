package com.benefit.model;

import android.location.Location;

/**
 * Model POJO for user
 */
public class User {

    private String UID;
    private String firstName;
    private String lastName;
    private String address;
    private Location location;
    private double rating;

    public User(String UID){
        this.UID = UID;
    }


}
