package com.benefit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.benefit.drivers.DatabaseDriver;
import com.benefit.viewmodel.SignInViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

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
            databaseDriver.startSignIn(sViewModel);
            return;
        }
    }

    private boolean shouldStartSignIn() {
        return (!sViewModel.getIsSigningIn() && !databaseDriver.isSignIn());
    }


}
