package com.benefit.model;

import com.google.firebase.firestore.ServerTimestamp;


import java.util.Date;

/**
 * Model POJO for product.
 */
public class Product {
    private String categoryId;
    private String SellerId;
    private String titleText;
    private String DescriptionText;
    private int viewCount;
    private int likesCount;
    private @ServerTimestamp Date auctionTimestamp;
}
