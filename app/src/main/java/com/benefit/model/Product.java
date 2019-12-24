package com.benefit.model;


import com.google.firebase.firestore.ServerTimestamp;


import java.util.Date;
import java.util.Map;

/**
 * Model POJO for product.
 */
public class Product {
    private String categoryId;
    private Map<String, String> propertiesAndValues;
    private String SellerId;
    private String titleText;
    private String DescriptionText;
    private int viewCount;
    private int likesCount;
    private @ServerTimestamp Date auctionTimestamp;
}
