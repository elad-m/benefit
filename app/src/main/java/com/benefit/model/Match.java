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

}
