package com.benefit.model;

import java.util.Date;

public class Product {
    private int id;
    private int categoryId;
    private int SellerId;
    private String title;
    private String description;
    private int views;
    private int likes;
    private Date auctionDate;

    public Product(int id, int categoryId, int sellerId, String title, String description, int views, int likes, Date auctionDate) {
        this.id = id;
        this.categoryId = categoryId;
        SellerId = sellerId;
        this.title = title;
        this.description = description;
        this.views = views;
        this.likes = likes;
        this.auctionDate = auctionDate;
    }


    public int getId() {
        return id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getSellerId() {
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
}
