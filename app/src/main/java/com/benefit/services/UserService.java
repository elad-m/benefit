package com.benefit.services;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.benefit.SignUpActivity;
import com.benefit.drivers.DatabaseDriver;
import com.benefit.model.User;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This class manage sign up and getting the user object.
 */
public class UserService extends ViewModel {
    private DatabaseDriver databaseDriver;
    private CollectionReference usersCollectionRef;
    private boolean mIsSigningIn;
    private MutableLiveData<User> user;
    private static final String COLLECTION_NAME = "users";
    private static final String TAG = UserService.class.getSimpleName();
    public static final int RC_SIGN_IN = 9001;

    public UserService(){
        this.databaseDriver = new DatabaseDriver();
        mIsSigningIn = false;
        usersCollectionRef = databaseDriver.getCollectionReferenceByName(COLLECTION_NAME);
    }

    public boolean isSignIn(){
        return databaseDriver.isSignIn();
    }

    public boolean shouldSignIn(){
        return !mIsSigningIn && isSignIn();
    }

    public String getUserUid(){
        return databaseDriver.getAuth().getUid();
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
        mIsSigningIn = true;
    }

    public void handleOnSignInResult(int signInRequestCode, int resultCode, AppCompatActivity activity){
        final List<User> documentsList = new LinkedList<>();
        mIsSigningIn = false;
        if(resultCode == Activity.RESULT_OK){
            Query getUserQuery = usersCollectionRef.whereEqualTo("uid", databaseDriver.getAuth().getUid());
            getUserQuery.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()){
                        Log.d(TAG, "User is not on database. Starting sign up for new user.");
                        Intent intent = new Intent(activity, SignUpActivity.class);
                        activity.startActivity(intent);
                    }
                    else {
                        for (QueryDocumentSnapshot document : task.getResult()){
                            documentsList.add(document.toObject(User.class));
                        }
                        user.setValue(documentsList.get(0));
                    }

                } else {
                    Log.d(TAG, "Error getting users documents: ", task.getException());
                }
            });
        }
        else{
            if (!databaseDriver.isSignIn()){
                startSignIn(activity, signInRequestCode);
            }
        }
    }

    public LiveData<User> getCurrentUser(){
        return user;
    }

    public void setCurrentUser(User user){
        this.user.setValue(user);
    }

}
