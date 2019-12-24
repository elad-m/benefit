package com.benefit.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

/**
 * Model POJO for a single chat massage.
 */
public class Chat {
    private String matchId;
    private String fromUserId;
    private String toUserId;
    private @ServerTimestamp Date timestamp;
    private String massageText;
    private List<String> imagesUrl;
    private List<String> keywords;
}
