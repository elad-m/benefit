package com.benefit.services;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.benefit.drivers.AuthenticationDriver;
import com.benefit.drivers.DatabaseDriver;

public class Factory {
    private final static DatabaseDriver databaseDriver = new DatabaseDriver();
    private final static AuthenticationDriver authenticationDriver = new AuthenticationDriver();

    public static ViewModelProvider.Factory getProductServiceFactory() {
        return new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new ProductService(databaseDriver);
            }
        };
    }

    public static ViewModelProvider.Factory getUserServiceFactory() {
        return new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new UserService(databaseDriver, authenticationDriver);
            }
        };
    }
}
