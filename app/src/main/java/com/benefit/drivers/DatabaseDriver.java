package com.benefit.drivers;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.content.Intent;

import com.benefit.viewmodel.SignInViewModel;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A driver for dealing with FireBase database
 */
public class DatabaseDriver {
    private FirebaseFirestore db;
    private static final String TAG = "DatabaseDriver";
    private FirebaseAuth auth;

    public DatabaseDriver() {
        this.db = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();

        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);

    }

    public boolean isSignIn(){
        return auth.getCurrentUser() != null;
    }

    public FirebaseFirestore getDb() {
        return this.db;
    }

    public FirebaseAuth getAuth(){ return this.auth;}

    public CollectionReference getCollectionByName(String name) {
        return this.db.collection(name);
    }

    public <T> LiveData<T> getSingleDocumentByField(String collectionName, String fieldName, Object fieldValue, final Class<T> typeParameterClass) {
        final List<T> documentsList = new LinkedList<>();
        final MutableLiveData<T> resultsLiveData = new MutableLiveData<>();
        getCollectionByName(collectionName)
                .whereEqualTo(fieldName, fieldValue)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            documentsList.add(document.toObject(typeParameterClass));
                        }
                        if (!documentsList.isEmpty()) {
                            resultsLiveData.setValue(documentsList.get(0));
                        }
                    }
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error on getSingleDocumentByField", e));
        return resultsLiveData;
    }

    public <T> LiveData<List<T>> getDocumentsByField(String collectionName, String fieldName, Object fieldValue, final Class<T> typeParameterClass) {
        return getDocumentsByField(collectionName, fieldName, Collections.singletonList(fieldValue), typeParameterClass);
    }

    public <T> LiveData<List<T>> getDocumentsByField(String collectionName, String fieldName, List<Object> fieldValue, final Class<T> typeParameterClass) {
        final List<T> documentsList = new LinkedList<>();
        final MutableLiveData<List<T>> resultsLiveData = new MutableLiveData<>();
        getCollectionByName(collectionName)
                .whereIn(fieldName, fieldValue)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            documentsList.add(document.toObject(typeParameterClass));
                        }
                        resultsLiveData.setValue(documentsList);
                    }
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error on getDocumentsByField", e));
        return resultsLiveData;
    }

    public LiveData<Boolean> deleteDocumentsByField(String collectionName, String fieldName, Object fieldValue) {
        final MutableLiveData<Boolean> resultsLiveData = new MutableLiveData<>();
        CollectionReference collectionReference = getCollectionByName(collectionName);
        Query query = collectionReference.whereEqualTo(fieldName, fieldValue);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    collectionReference.document(document.getId()).delete();
                }
                resultsLiveData.setValue(true);
            } else {
                Log.d(TAG, "Error getting documents in deleteDocumentsByField", task.getException());
                resultsLiveData.setValue(false);
            }
        });
        return resultsLiveData;
    }
}
