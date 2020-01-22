package com.benefit.model;

import com.benefit.ui.Displayable;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Model POJO for product
 */
public class Product implements Serializable, Displayable {
    private int id;
    private int categoryId;
    private String sellerId;
    private String title;
    private String description;
    private int views;
    private int likes;
    private @ServerTimestamp
    Date auctionDate;
    private Map<String, List<String>> properties;
    private List<String> imagesUrls;

    public Product() {
    }

    public Product(int id, int categoryId, String sellerId, String title, String description, int views, int likes, Date auctionDate, Map<String, List<String>> properties, List<String> imagesUrls) {
        this.id = id;
        this.categoryId = categoryId;
        this.sellerId = sellerId;
        this.title = title;
        this.description = description;
        this.views = views;
        this.likes = likes;
        this.auctionDate = auctionDate;
        this.properties = properties;
        this.imagesUrls = imagesUrls;
    }


    public int getId() {
        return id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String getName() {
        return getTitle();
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

    public List<String> getImagesUrls() {
        return imagesUrls;
    }

    @Override
    public String getImageResource() {
        return imagesUrls.get(0);
    }
}
