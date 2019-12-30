package com.benefit.viewmodel;

import androidx.lifecycle.ViewModel;

/**
 * ViewModel for SignInActivity. Keep data members in a lifecycle conscious way.
 */
public class SignInViewModel extends ViewModel {

    private boolean mIsSigningIn;

    public SignInViewModel(){
        mIsSigningIn = false;
    }

    public boolean getIsSigningIn() {
        return mIsSigningIn;
    }

    public void setIsSigningIn(boolean mIsSigningIn) {
        this.mIsSigningIn = mIsSigningIn;
    }
}
