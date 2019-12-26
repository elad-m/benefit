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

    public Chat(){}

    public Chat(String matchId, String fromUserId, String toUserId, String massageText){
        this.matchId = matchId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.massageText = massageText;
    }

    public String getMatchId() {
        return matchId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMassageText() {
        return massageText;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setMassageText(String massageText) {
        this.massageText = massageText;
    }
}
