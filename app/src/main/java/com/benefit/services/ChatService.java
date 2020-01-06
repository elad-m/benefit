package com.benefit.services;


import android.util.Log;

import androidx.annotation.NonNull;

import com.benefit.drivers.DatabaseDriver;
import com.benefit.model.Chat;
import com.benefit.model.Match;
import com.benefit.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ChatService {

    private static final String TAG = ChatService.class.getSimpleName();

    private DatabaseDriver databaseDriver;
    private User user;
    private CollectionReference matchCollectionRef;
    private List<Match> matchesWithBuyers;
    private Map<Match,List<Chat>> conversationWithBuyers;
    private List<Match> matchesWithSellers;
    private Map<Match,List<Chat>> conversationWithSellers;
    private boolean sellersDataIsReady;
    private boolean buyersDataIsReady;

    public ChatService(DatabaseDriver databaseDriver, final User user){
        this.databaseDriver = databaseDriver;
        this.user = user;
        if (!databaseDriver.isSignIn() || !databaseDriver.getAuth().getUid().equals(user.getUid())){
            Log.d(TAG, "error: user model object is not matching current firebase user.");
        }
        matchCollectionRef = databaseDriver.getCollectionReferenceByName("matches");
        sellersDataIsReady = false;
        matchesWithSellers = new ArrayList<>();
        conversationWithSellers = new HashMap<>();
        matchCollectionRef.whereEqualTo("buyerId", user.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Match currentMatch = document.toObject(Match.class);
                        matchesWithSellers.add(currentMatch);
                    }
                    sellersDataIsReady = true;
                }
                else {
                    Log.d(TAG, "Error getting matches with buyerId: " + user.getUid());
                }
            }
        });
        buyersDataIsReady = false;
        matchesWithBuyers = new ArrayList<>();
        matchCollectionRef.whereEqualTo("sellerId", user.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        matchesWithBuyers.add(document.toObject(Match.class));
                    }
                    buyersDataIsReady = true;
                }
                else {
                    Log.d(TAG, "Error getting matches with sellerId: " + user.getUid());
                }
            }
        });
    }

    public void addMatch(Match match){
        matchCollectionRef.add(match);
    }


}
