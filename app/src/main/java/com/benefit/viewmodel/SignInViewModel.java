package com.benefit.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.benefit.SignInActivity;
import com.benefit.drivers.AuthenticationDriver;
import com.benefit.drivers.DatabaseDriver;
import com.benefit.model.User;
import com.benefit.services.UserService;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.LinkedList;
import java.util.List;

public class SignInViewModel extends ViewModel {

    private static final String TAG = SignInViewModel.class.getSimpleName();

    private User user;
    private DatabaseDriver databaseDriver;
    private AuthenticationDriver authenticationDriver;
    private SignInActivity.LoginState loginState;

    private Boolean mAccessingDatabase;
    private UserService userService;

    public SignInViewModel() {
        mAccessingDatabase = false;
        databaseDriver = new DatabaseDriver();
        authenticationDriver = new AuthenticationDriver();
        userService = new UserService(databaseDriver, authenticationDriver);
        if (authenticationDriver.isSignIn()) {
            loginState = SignInActivity.LoginState.SIGN_IN_GET_USER;
        } else {
            loginState = SignInActivity.LoginState.NOT_SIGN_IN;
        }
    }

    public User getUser() {
        return user;
    }

    public void createNewUser(){
        user = new User(authenticationDriver.getUserUid());
    }

    public void addNewUserToDatabase(){
        userService.addUserToDatabase(user);
    }

    public Boolean getmAccessingDatabase() {
        return mAccessingDatabase;
    }

    public SignInActivity.LoginState getLoginState() {
        return loginState;
    }

    public void setLoginState(SignInActivity.LoginState loginState) {
        this.loginState = loginState;
    }

    public LiveData<Boolean> signInWithGoogle(GoogleSignInAccount account) {
        MutableLiveData<Boolean> success = new MutableLiveData<>();
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        authenticationDriver.getAuth().signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update ui with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        success.setValue(true);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        success.setValue(false);
                    }
                });
        return success;
    }

    public LiveData<Boolean> getUserFromDatabase(){
        MutableLiveData<Boolean> success = new MutableLiveData<>();
        final List<User> documentsList = new LinkedList<>();
        mAccessingDatabase = true;
        Query getUserQuery = databaseDriver.getCollectionReferenceByName(UserService.COLLECTION_USERS_NAME).whereEqualTo(UserService.UID, authenticationDriver.getUserUid());
        getUserQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    Log.d(TAG, "User is not on database. Starting sign up for new user.");
                    user = null;
                } else {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        documentsList.add(document.toObject(User.class));
                    }
                    user = documentsList.get(0);
                }
                success.setValue(true);
            } else {
                Log.d(TAG, "Error getting users documents: ", task.getException());
                success.setValue(false);
            }
            mAccessingDatabase = false;
        });

        return success;
    }

}
