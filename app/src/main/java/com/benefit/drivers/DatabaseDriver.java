package com.benefit.drivers;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This class is a "black box" for Firebase.
 */
public class DatabaseDriver {
    private FirebaseFirestore db;

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
}
