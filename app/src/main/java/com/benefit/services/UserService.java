package com.benefit.services;

import android.app.Activity;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;

import com.benefit.drivers.DatabaseDriver;
import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;

/**
 * This class manage sign up and getting the user object.
 */
public class UserService extends ViewModel {
    private DatabaseDriver databaseDriver;
    private boolean mIsSignIn;

    public UserService(){
        this.databaseDriver = new DatabaseDriver();
        mIsSignIn = databaseDriver.isSignIn();
    }

    public boolean isSignIn(){
        return (!mIsSignIn && databaseDriver.isSignIn());
    }

    public void startSignIn(AppCompatActivity activity, int signInRequestCode){
        Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.PhoneBuilder().build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build()))
                .setIsSmartLockEnabled(false)
                .build();
        activity.startActivityForResult(intent, signInRequestCode);
        mIsSignIn = true;
    }

    public void handleOnSignInResult(int resultCode){
        mIsSignIn = false;
        if(resultCode == Activity.RESULT_OK && databaseDriver.isSignIn()){

        }

    }

}
