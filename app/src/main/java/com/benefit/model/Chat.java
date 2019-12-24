package com.benefit.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Chat {
    private String matchId;
    private String fromUserId;
    private String toUserId;
    private @ServerTimestamp Date timestamp;
    private String massageText;
}
