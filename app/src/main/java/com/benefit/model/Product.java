package com.benefit.model;

import android.util.Pair;

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
    private String SellerId;
    private String titleText;
    private String DescriptionText;
    private int viewCount;
    private int likesCount;
    private @ServerTimestamp Date auctionTimestamp;
}
