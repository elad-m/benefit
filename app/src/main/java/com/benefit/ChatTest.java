package com.benefit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Collections;

public class ChatTest extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;

    private FirebaseFirestore mFirestore;
    private boolean isSignIn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_test);

        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);
        // Initialize Firestore
        mFirestore = FirebaseFirestore.getInstance();

        isSignIn = false;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!isSignIn){
            startSignIn();
            return;
        }
    }

    public void startSignIn(){
        Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(Collections.singletonList(
                        new AuthUI.IdpConfig.EmailBuilder().build()))
                .setIsSmartLockEnabled(false)
                .build();

        startActivityForResult(intent, RC_SIGN_IN);
        isSignIn = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            isSignIn = false;

            if (resultCode != RESULT_OK && FirebaseAuth.getInstance().getCurrentUser() == null) {
                startSignIn();
            }
        }
    }

}
