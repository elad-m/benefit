package com.benefit.utilities;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.benefit.drivers.AuthenticationDriver;
import com.benefit.drivers.DatabaseDriver;
import com.benefit.drivers.StorageDriver;
import com.benefit.services.CategoryService;
import com.benefit.services.ProductService;
import com.benefit.services.SearchService;
import com.benefit.services.UserService;

/**
 * A static class which generates factories for the different services
 */
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

    public static ViewModelProvider.Factory getCategoryServiceFactory() {
        return new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new CategoryService(databaseDriver);
            }
        };
    }

    public static ViewModelProvider.Factory getSearchServiceFactory() {
        return new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new SearchService(databaseDriver);
            }
        };
    }

    public static ViewModelProvider.Factory getStorageDriverFactory() {
        return new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new StorageDriver();
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
