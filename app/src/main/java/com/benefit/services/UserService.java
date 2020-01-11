package com.benefit.services;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
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
    public static final int RC_SIGN_UP = 9002;

    public UserService() {
        this.databaseDriver = new DatabaseDriver();
        mIsSigningIn = false;
        usersCollectionRef = databaseDriver.getCollectionReferenceByName(COLLECTION_NAME);
    }

    public boolean isSignIn() {
        return databaseDriver.isSignIn();
    }

    public boolean shouldSignIn() {
        return (!mIsSigningIn && !isSignIn());
    }

    public String getUserUid() {
        return databaseDriver.getAuth().getUid();
    }

    public void startSignIn(AppCompatActivity activity) {
        Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.PhoneBuilder().build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build()))
                .setIsSmartLockEnabled(false)
                .build();
        activity.startActivityForResult(intent, RC_SIGN_IN);
        mIsSigningIn = true;
    }

    public void handleOnSignInResult(int requestCode, int resultCode, Intent data, AppCompatActivity activity) {
        if (requestCode == RC_SIGN_IN) {
            final List<User> documentsList = new LinkedList<>();
            if (resultCode == Activity.RESULT_OK) {
                Query getUserQuery = usersCollectionRef.whereEqualTo("uid", getUserUid());
                getUserQuery.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            Log.d(TAG, "User is not on database. Starting sign up for new user.");
                            Intent intent = new Intent(activity, SignUpActivity.class);
                            activity.startActivityForResult(intent, RC_SIGN_UP);
                        } else {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                documentsList.add(document.toObject(User.class));
                            }
                            user.setValue(documentsList.get(0));
                            mIsSigningIn = false;
                        }
                    } else {
                        Log.d(TAG, "Error getting users documents: ", task.getException());
                    }
                });
            } else {
                if (!databaseDriver.isSignIn()) {
                    mIsSigningIn = false;
                    startSignIn(activity);
                }
            }
        }
        if (requestCode == RC_SIGN_UP) {
            if (resultCode == Activity.RESULT_OK) {
                mIsSigningIn = false;
                User newUser = new User(databaseDriver.getAuth().getUid());
                newUser.setFirstName(data.getStringExtra(SignUpActivity.USER_FIRST_NAME_REPLY));
                newUser.setLastName(data.getStringExtra(SignUpActivity.USER_LAST_NAME_REPLY));
                newUser.setAddress(data.getStringExtra(SignUpActivity.USER_ADDRESS_NAME_REPLY));
                newUser.setRating(0);
                final Location newUserLocation = (Location) data.getParcelableArrayListExtra(SignUpActivity.USER_LOCATION_REPLY).get(0);
                newUser.setLocationLatitude(newUserLocation.getLatitude());
                newUser.setLocationLongitude(newUserLocation.getLongitude());
                user.setValue(newUser);
                usersCollectionRef.add(newUser);
            } else {
                Log.d(TAG, "Sign up fail. Lunching Sign up activity again.");
                Intent intent = new Intent(activity, SignUpActivity.class);
                activity.startActivityForResult(intent, RC_SIGN_UP);
            }
        }
    }

    public LiveData<User> getCurrentUser() {
        if (user == null) {
            user = new MutableLiveData<>();
            if (!shouldSignIn()) {
                final List<User> documentsList = new LinkedList<>();
                Query getUserQuery = usersCollectionRef.whereEqualTo("uid", getUserUid());
                getUserQuery.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            documentsList.add(document.toObject(User.class));
                        }
                        user.setValue(documentsList.get(0));
                    } else {
                        Log.d(TAG, "Error getting user object: ", task.getException());
                    }
                });
            }
        }
        return user;
    }


}
