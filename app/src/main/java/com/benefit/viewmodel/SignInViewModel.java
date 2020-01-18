package com.benefit.viewmodel;

import androidx.lifecycle.ViewModel;

import com.benefit.drivers.AuthenticationDriver;
import com.benefit.drivers.DatabaseDriver;
import com.benefit.model.User;

public class SignInViewModel extends ViewModel {
    private User user;
    private DatabaseDriver databaseDriver;
    private AuthenticationDriver authenticationDriver;

    public SignInViewModel(){
        databaseDriver = new DatabaseDriver();
        authenticationDriver = new AuthenticationDriver();
    }

    public User getUser() {
        return user;
    }
}
