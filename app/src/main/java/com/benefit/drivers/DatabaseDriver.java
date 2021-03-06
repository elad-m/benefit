package com.benefit.drivers;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
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
    }

    public FirebaseFirestore getDb() {
        return this.db;
    }

    public CollectionReference getCollectionReferenceByName(String name) {
        return this.db.collection(name);
    }

    public <T> LiveData<T> getSingleDocumentByField(String collectionName, String fieldName, Object fieldValue, final Class<T> typeParameterClass) {
        final List<T> documentsList = new LinkedList<>();
        final MutableLiveData<T> resultsLiveData = new MutableLiveData<>();
        getCollectionReferenceByName(collectionName)
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
        getCollectionReferenceByName(collectionName)
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
        CollectionReference collectionReference = getCollectionReferenceByName(collectionName);
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
