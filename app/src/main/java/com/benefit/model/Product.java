package com.benefit.model;


import com.google.firebase.firestore.ServerTimestamp;


import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Model POJO for product.
 */
public class Product {
    private String categoryId;
    private Map<String, String> propertiesAndValues;
    private String sellerId;
    private String titleText;
    private String descriptionText;
    private int viewCount = 0;
    private int likesCount = 0;
    private @ServerTimestamp Date auctionTimestamp;
    private List<String> imagesUrl;
    private List<String> keywords;

    public Product(){}

    public Product(String categoryId, String sellerId, String titleText){
        this.categoryId = categoryId;
        this.sellerId = sellerId;
        this.titleText = titleText;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public Map<String, String> getPropertiesAndValues() {
        return propertiesAndValues;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getTitleText() {
        return titleText;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public int getViewCount() {
        return viewCount;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public Date getAuctionTimestamp() {
        return auctionTimestamp;
    }

    public List<String> getImagesUrl() {
        return imagesUrl;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public void setDescriptionText(String descriptionText) {
        this.descriptionText = descriptionText;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public void setAuctionTimestamp(Date auctionTimestamp) {
        this.auctionTimestamp = auctionTimestamp;
    }

    public void setImagesUrl(List<String> imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public void setPropertiesAndValues(Map<String, String> propertiesAndValues) {
        this.propertiesAndValues = propertiesAndValues;
    }
}
