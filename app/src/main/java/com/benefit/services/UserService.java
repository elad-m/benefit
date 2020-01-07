package com.benefit.services;

import androidx.lifecycle.ViewModel;

import com.benefit.drivers.DatabaseDriver;

/**
 * This class manage sign up and getting the user object.
 */
public class UserService extends ViewModel {
    private DatabaseDriver databaseDriver;
    private boolean mIsSignIn;

    public UserService(DatabaseDriver databaseDriver){
        this.databaseDriver = databaseDriver;
        mIsSignIn = databaseDriver.isSignIn();
    }

    public boolean isSignIn(){
        return (!mIsSignIn && databaseDriver.isSignIn());
    }

}
