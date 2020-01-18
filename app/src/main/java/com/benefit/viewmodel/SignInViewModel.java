package com.benefit.viewmodel;

import androidx.lifecycle.ViewModel;

import com.benefit.SignInActivity;
import com.benefit.drivers.AuthenticationDriver;
import com.benefit.drivers.DatabaseDriver;
import com.benefit.model.User;
import com.benefit.services.UserService;

public class SignInViewModel extends ViewModel {
    private User user;
    private DatabaseDriver databaseDriver;
    private AuthenticationDriver authenticationDriver;
    private SignInActivity.LoginState loginState;

    private UserService userService;

    public SignInViewModel(){
        databaseDriver = new DatabaseDriver();
        authenticationDriver = new AuthenticationDriver();
        userService = new UserService(databaseDriver, authenticationDriver);
        if (authenticationDriver.isSignIn()){
            loginState = SignInActivity.LoginState.SIGN_IN_GET_USER;
        }
        else {
            loginState = SignInActivity.LoginState.NOT_SIGN_IN;
        }
    }

    public User getUser() {
        return user;
    }

    public SignInActivity.LoginState getLoginState() {
        return loginState;
    }

    public void setLoginState(SignInActivity.LoginState loginState) {
        this.loginState = loginState;
    }
}
