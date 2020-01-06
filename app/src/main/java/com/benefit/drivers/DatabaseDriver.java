package com.benefit.drivers;

import android.content.Intent;

import com.benefit.viewmodel.SignInViewModel;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class is a "black box" for Firebase.
 */
public class DatabaseDriver {
    private FirebaseFirestore db;
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

    public CollectionReference getCollectionReferenceByName(String name) {
        return this.db.collection(name);
    }
}
