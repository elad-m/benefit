package com.benefit.drivers;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * A driver for dealing with FireBase database
 */
public class DatabaseDriver {
    private FirebaseFirestore db;
    private static final String TAG = "DatabaseDriver";

    public DatabaseDriver() {
        this.db = FirebaseFirestore.getInstance();
        FirebaseFirestore.setLoggingEnabled(true);
    }

    public FirebaseFirestore getDb() {
        return this.db;
    }

    public CollectionReference getCollectionByName(String name) {
        return this.db.collection(name);
    }

    public <T> MutableLiveData<T> getSingleDocumentByField(String collectionName, String fieldName, Object fieldValue, final Class<T> typeParameterClass) {
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

    public <T> MutableLiveData<List<T>> getDocumentsByField(String collectionName, String fieldName, Object fieldValue, final Class<T> typeParameterClass) {
        return getDocumentsByField(collectionName, fieldName, Collections.singletonList(fieldValue), typeParameterClass);
    }

    public <T> MutableLiveData<List<T>> getDocumentsByField(String collectionName, String fieldName, List<Object> fieldValue, final Class<T> typeParameterClass) {
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
}
