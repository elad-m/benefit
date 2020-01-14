package com.benefit.services;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.benefit.drivers.AuthenticationDriver;
import com.benefit.drivers.DatabaseDriver;
import com.benefit.model.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.LinkedList;
import java.util.List;

/**
 * This class manage sign up and getting the user object.
 */
public class UserService extends ViewModel {
    private DatabaseDriver databaseDriver;
    private AuthenticationDriver authenticationDriver;
    private CollectionReference usersCollectionRef;
    public static final String COLLECTION_USERS_NAME = "users";
    public static final String UID = "uid";
    private static final String TAG = UserService.class.getSimpleName();

    public UserService(DatabaseDriver databaseDriver, AuthenticationDriver authenticationDriver) {
        this.databaseDriver = databaseDriver;
        this.authenticationDriver = authenticationDriver;
        usersCollectionRef = databaseDriver.getCollectionReferenceByName(COLLECTION_USERS_NAME);
    }

    public boolean isSignIn() {
        return authenticationDriver.isSignIn();
    }

    public String getCurrentUserUid() {
        return authenticationDriver.getUserUid();
    }


    public LiveData<User> getUserFromUid(String uid) {
        MutableLiveData<User> user = new MutableLiveData<>();
        final List<User> documentsList = new LinkedList<>();
        Query getUserQuery = usersCollectionRef.whereEqualTo(UID, uid);
        getUserQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    Log.d(TAG, "User is not on database. Starting sign up for new user.");
                    user.setValue(null);
                } else {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        documentsList.add(document.toObject(User.class));
                    }
                    user.setValue(documentsList.get(0));
                }
            } else {
                Log.d(TAG, "Error getting users documents: ", task.getException());
            }
        });
        return user;
    }

    public LiveData<User> getCurrentUser() {
        return getUserFromUid(authenticationDriver.getUserUid());
    }

    public void addUserToDatabase(User user){
        usersCollectionRef.add(user);
    }

    public LiveData<User> getUserById(String userId) {
        return databaseDriver.getSingleDocumentByField(COLLECTION_USERS_NAME, UID, userId, User.class);
    }

    public LiveData<User> getUserById(String userId) {
        return databaseDriver.getSingleDocumentByField(COLLECTION_USERS_NAME, UID, userId, User.class);
    }


}