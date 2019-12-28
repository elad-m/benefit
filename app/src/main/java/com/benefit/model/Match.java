package com.benefit.model;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

/**
 * Model POJO for match between a user and an product.
 */
public class Match {
    private int id;
    private String SellerId;
    private String BuyerId;
    private int productId;
    private @ServerTimestamp Date timestamp;
    private boolean isClosed = false;

    public Match(){}

    public Match(int id, String sellerId, String buyerId, int productId){
        this.id = id;
        this.SellerId = sellerId;
        this.BuyerId = buyerId;
        this.productId = productId;
    }

    public String getSellerId() {
        return SellerId;
    }

    public String getBuyerId() {
        return BuyerId;
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
}
