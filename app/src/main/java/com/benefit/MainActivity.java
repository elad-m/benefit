package com.benefit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.benefit.drivers.DatabaseDriver;
import com.benefit.model.Category;
import com.benefit.model.Product;
import com.benefit.services.CategoryService;
import com.benefit.services.ProductService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private CategoryService categoryService;
    private ProductService productService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseDriver databaseDriver = new DatabaseDriver();
        ViewModelProvider.Factory factory = new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T)new CategoryService(databaseDriver);
            }
        };
        ViewModelProvider.Factory factory2 = new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T)new ProductService(databaseDriver);
            }
        };
        categoryService = ViewModelProviders.of(this, factory).get(CategoryService.class);
        productService = ViewModelProviders.of(this, factory2).get(ProductService.class);
        // Create the observer which updates the UI.
        final Observer<Category> nameObserver = new Observer<Category>(){

            @Override
            public void onChanged(Category categories) {
                Category c = categories;
            }
        };
        final Observer<List<Product>> nameObserver2 = new Observer<List<Product>>(){

            @Override
            public void onChanged(List<Product> categories) {
                List<Product> c = categories;
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        Map<String, String> m = new HashMap<>();
        m.put("asd", "apple");
        categoryService.getCategoryById(1).observe(this, nameObserver);
        productService.getProductsByProperties(1, m).observe(this, nameObserver2);
    }

}
