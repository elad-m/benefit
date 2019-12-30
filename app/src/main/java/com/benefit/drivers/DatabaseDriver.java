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
    private List<AuthUI.IdpConfig> authProviders;

    public DatabaseDriver() {
        this.db = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();

        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);

        //providers for login
        authProviders = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build());
    }

    public boolean isSignIn(){
        return auth.getCurrentUser() != null;
    }

    public void startSignIn(SignInViewModel signInViewModel){
        // Sign in with FirebaseUI
        Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(authProviders)
                .setIsSmartLockEnabled(false)
                .build();

        startActivityForResult(intent, RC_SIGN_IN);
        signInViewModel.setIsSigningIn(true);

    }

    public FirebaseFirestore getDb() {
        return this.db;
    }

    public CollectionReference getCollectionByName(String name) {
        return this.db.collection(name);
    }
}
