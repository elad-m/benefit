package com.benefit.drivers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A driver for dealing with FireBase authentication
 */
public class AuthenticationDriver{
    private FirebaseAuth auth;

    public AuthenticationDriver(){
        this.auth = FirebaseAuth.getInstance();

        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);
    }

    public boolean isSignIn(){
        return auth.getCurrentUser() != null;
    }

    public FirebaseAuth getAuth(){ return this.auth;}

    public String getUserUid(){
        return auth.getUid();
    }

}
