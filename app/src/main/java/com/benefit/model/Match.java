package com.benefit.model;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

/**
 * Model POJO for match between a user and an product.
 */
public class Match {
    private String sellerId;
    private String buyerId;
    private String productId;
    private @ServerTimestamp Date timestamp;
    private boolean isClosed = false;

    public Match(){}

    public Match(String sellerId, String buyerId, String productId){
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.productId = productId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public String getProductId() {
        return productId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }
}
