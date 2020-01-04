package com.benefit.drivers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class DatabaseDriver {
    private FirebaseFirestore db;
    private static final String TAG = "DatabaseDriver";

    public DatabaseDriver() {
        this.db = FirebaseFirestore.getInstance();
    }

    public FirebaseFirestore getDb() {
        return this.db;
    }

    public CollectionReference getCollectionByName(String name) {
        return this.db.collection(name);
    }

    public <T> List<T> getDocumentsByField(String collectionName, String FieldName, Object FieldValue, final Class<T> typeParameterClass) {
        final List<T> documentsList = new LinkedList<>();
        getCollectionByName(collectionName)
                .whereEqualTo(FieldName, FieldValue)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                documentsList.add(document.toObject(typeParameterClass));
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error on getDocumentsByField", e);
                    }
                });
        return documentsList;
    }
}
