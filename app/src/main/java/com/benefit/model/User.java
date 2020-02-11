package com.benefit.model;


import java.io.Serializable;

/**
 * Model POJO for user with location and rating.
 */
public class User implements Serializable{

    private String uid;
    private String nickname;
    private String city;
    private String phoneNumber;
    private double locationLatitude;
    private double locationLongitude;
    private double rating;

    public User(){}

    public User(String uid){
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public String getNickname() {
        return nickname;
    }

    public String getCity() {
        return city;
    }

    public double getRating() {
        return rating;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
