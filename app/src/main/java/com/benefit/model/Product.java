package com.benefit.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Product {
    private int id;
    private int categoryId;
    private String SellerId;
    private String title;
    private String description;
    private int views;
    private int likes;
    private @ServerTimestamp Date auctionDate;
    private Map<String, List<String>> properties;

    public Product(int id, int categoryId, String sellerId, String title, String description, int views, int likes, Date auctionDate, Map<String, List<String>> properties) {
        this.id = id;
        this.categoryId = categoryId;
        SellerId = sellerId;
        this.title = title;
        this.description = description;
        this.views = views;
        this.likes = likes;
        this.auctionDate = auctionDate;
        this.properties = properties;
    }


    public int getId() {
        return id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getSellerId() {
        return SellerId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getViews() {
        return views;
    }

    public int getLikes() {
        return likes;
    }

    public Date getAuctionDate() {
        return auctionDate;
    }

    public Map<String, List<String>> getProperties() {
        return properties;
    }
}
