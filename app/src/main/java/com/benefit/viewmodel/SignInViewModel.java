package com.benefit.viewmodel;

import androidx.lifecycle.ViewModel;

import com.benefit.drivers.AuthenticationDriver;
import com.benefit.drivers.DatabaseDriver;
import com.benefit.model.User;
import com.benefit.services.UserService;

public class SignInViewModel extends ViewModel {
    private User user;
    private DatabaseDriver databaseDriver;
    private AuthenticationDriver authenticationDriver;

    private UserService userService;

    public SignInViewModel(){
        databaseDriver = new DatabaseDriver();
        authenticationDriver = new AuthenticationDriver();
        userService = new UserService(databaseDriver, authenticationDriver);
    }

    public User getUser() {
        return user;
    }
}
