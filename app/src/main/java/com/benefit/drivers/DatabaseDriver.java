package com.benefit.drivers;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DatabaseDriver {
    private FirebaseFirestore db;

    public DatabaseDriver() {
        this.db = FirebaseFirestore.getInstance();
    }

    public FirebaseFirestore getDb() {
        return this.db;
    }

    public CollectionReference getCollectionByName(String name) {
        return this.db.collection(name);
    }
}
