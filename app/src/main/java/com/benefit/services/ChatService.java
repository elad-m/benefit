package com.benefit.services;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.benefit.adapters.ConversationAdapter;
import com.benefit.drivers.DatabaseDriver;
import com.benefit.model.Chat;
import com.benefit.model.Match;
import com.benefit.model.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ChatService extends ViewModel {

    private static final String TAG = ChatService.class.getSimpleName();

    private DatabaseDriver databaseDriver;
    private User user;
    private CollectionReference matchCollectionRef;
    private List<Match> matchesWithBuyers;
    private List<Match> matchesWithSellers;

    public ChatService(){
        this.databaseDriver = new DatabaseDriver();
        matchCollectionRef = databaseDriver.getCollectionReferenceByName("matches");
        matchesWithSellers = new ArrayList<>();
        matchCollectionRef.whereEqualTo("buyerId", user.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Match currentMatch = document.toObject(Match.class);
                        matchesWithSellers.add(currentMatch);
                    }
                }
                else {
                    Log.d(TAG, "Error getting matches with buyerId: " + user.getUid());
                }
            }
        });
        matchesWithBuyers = new ArrayList<>();
        matchCollectionRef.whereEqualTo("sellerId", user.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        matchesWithBuyers.add(document.toObject(Match.class));
                    }
                }
                else {
                    Log.d(TAG, "Error getting matches with sellerId: " + user.getUid());
                }
            }
        });
    }

    public void setUser(User user){
        if (user !=  null){
            this.user = user;
        }
        else {
            Log.d(TAG, "Error - user is null.");
        }
    }


    public void addMatch(Match match){
        matchCollectionRef.add(match);
    }

    public ConversationAdapter getConversationAdaptor(boolean wantedProducts, boolean userProducts, boolean orderNewToOld){
        if (user == null){
            Log.d(TAG, "Error - user is null. Can't return a ConversationAdaptor.");
            return null;
        }
        Query matchQuery;
        if (wantedProducts && !userProducts){
            matchQuery = matchCollectionRef.whereEqualTo("buyerId", user.getUid());
        }
        else if (!wantedProducts && userProducts){
            matchQuery = matchCollectionRef.whereEqualTo("sellerId", user.getUid());
        }
        else {
            matchQuery = matchCollectionRef.whereArrayContains("usersId", user.getUid());
        }
        matchQuery.whereEqualTo("isClosed", false);
        if (orderNewToOld){
            matchQuery.orderBy("timestamp", Query.Direction.DESCENDING);
        }
        else {
            matchQuery.orderBy("timestamp", Query.Direction.ASCENDING);
        }
        FirestoreRecyclerOptions<Match> matchRecyclerOptions = new FirestoreRecyclerOptions.Builder<Match>().setQuery(matchQuery, Match.class).build();
        return new ConversationAdapter(matchRecyclerOptions, databaseDriver);
    }


}
