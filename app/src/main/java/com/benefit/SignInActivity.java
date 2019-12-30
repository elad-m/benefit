package com.benefit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.benefit.drivers.DatabaseDriver;
import com.benefit.viewmodel.SignInViewModel;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = SignInViewModel.class.getSimpleName();
    private static final int RC_SIGN_IN = 9001;

    private DatabaseDriver databaseDriver;
    private SignInViewModel sViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        databaseDriver = new DatabaseDriver();

        // View model
        sViewModel = ViewModelProviders.of(this).get(SignInViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (shouldStartSignIn()){
            startSignIn();
            return;
        }
    }

    private void startSignIn(){
        Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build()))
                .setIsSmartLockEnabled(false)
                .build();
        startActivityForResult(intent, RC_SIGN_IN);
        sViewModel.setIsSigningIn(true);
    }

    private boolean shouldStartSignIn() {
        return (!sViewModel.getIsSigningIn() && !databaseDriver.isSignIn());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            sViewModel.setIsSigningIn(false);

            if (resultCode != RESULT_OK && !databaseDriver.isSignIn()) {
                startSignIn();
            }
            else {
                Query getUserQuery = databaseDriver.getCollectionByName("users").whereEqualTo("UID", databaseDriver.getAuth().getUid());
                getUserQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()){
                                Log.d(TAG, "User is not on database. adding user to data base.");
                            }
                            else {
                                for (QueryDocumentSnapshot document : task.getResult()){

                                }
                            }

                        } else {
                            Log.d(TAG, "Error getting users documents: ", task.getException());
                        }
                    }
                });
            }
        }
    }


}
