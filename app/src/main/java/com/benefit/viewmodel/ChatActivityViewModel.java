package com.benefit.viewmodel;

import androidx.lifecycle.ViewModel;

/**
 * viewModel for ChatActivity. Keep data members in a lifecycle conscious way.
 */
public class ChatActivityViewModel extends ViewModel {

    private boolean mIsSigningIn;

    public ChatActivityViewModel(){
        mIsSigningIn = false;
    }

    public boolean getIsSigningIn() {
        return mIsSigningIn;
    }

    public void setIsSigningIn(boolean mIsSigningIn) {
        this.mIsSigningIn = mIsSigningIn;
    }
}
