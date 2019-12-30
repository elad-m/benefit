package com.benefit.viewmodel;

import androidx.lifecycle.ViewModel;

/**
 * ViewModel for MainActivity. Keep data members in a lifecycle conscious way.
 */
public class MainActivityViewModel extends ViewModel {

    private boolean mIsSigningIn;

    public MainActivityViewModel(){
        mIsSigningIn = false;
    }

    public boolean getIsSigningIn() {
        return mIsSigningIn;
    }

    public void setIsSigningIn(boolean mIsSigningIn) {
        this.mIsSigningIn = mIsSigningIn;
    }
}
