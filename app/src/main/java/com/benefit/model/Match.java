package com.benefit.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Model POJO for match between a user and an product.
 */
public class Match {
    private int id;
    private String sellerId;
    private String buyerId;
    private List<String> usersId;
    private int productId;
    private @ServerTimestamp Date timestamp;
    private boolean isClosed;

    public Match(){
        this.isClosed = false;
        this.usersId = new ArrayList<>();
        usersId.add(sellerId);
        usersId.add(buyerId);
    }

    public Match(int id, String sellerId, String buyerId, int productId){
        this.id = id;
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.productId = productId;
        this.isClosed = false;
        this.usersId = Arrays.asList(sellerId, buyerId);
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public int getProductId() {
        return productId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public int getId() {
        return id;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public List<String> getUsersId() {
        return usersId;
    }
}
