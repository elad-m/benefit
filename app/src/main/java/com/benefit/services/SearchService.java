package com.benefit.services;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.benefit.drivers.DatabaseDriver;
import com.benefit.model.Product;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * A service which responsible for search procedures
 */
public class SearchService extends ViewModel {
    private DatabaseDriver databaseDriver;
    private CollectionReference productsCollection;
    private static final String COLLECTION_NAME_PRODUCTS = "products";
    private static final String TAG = "SearchService";

    public SearchService(DatabaseDriver databaseDriver) {
        this.databaseDriver = databaseDriver;
        this.productsCollection = this.databaseDriver.getCollectionByName(COLLECTION_NAME_PRODUCTS);
    }

    public MutableLiveData<List<Product>> getProductsBySearchString(String searchQuery) {
        return getProductsBySearchString(searchQuery, -1);
    }

    public MutableLiveData<List<Product>> getProductsBySearchString(String searchQuery, int categoryId) {
        String[] keywords = tokenizeString(searchQuery);
        final List<Product> productsList = new LinkedList<>();
        final MutableLiveData<List<Product>> resultsLiveData = new MutableLiveData<>();
        Task<QuerySnapshot> querySnapshotTask;
        if (categoryId < 0) {
            querySnapshotTask = this.productsCollection.get();
        } else {
            querySnapshotTask = this.productsCollection.whereEqualTo("categoryId", categoryId).get();

        }
        querySnapshotTask
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Product product = document.toObject(Product.class);
                            if (isProductFitsKeywords(product, keywords)) {
                                productsList.add(product);
                            }
                        }
                        resultsLiveData.setValue(productsList);
                    }
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error on getProductsBySearchString", e));
        return resultsLiveData;
    }

    private String[] tokenizeString(String string) {
        String[] tokenized = string.split(" ");
        for (int i = 0; i < tokenized.length; i++) {
            tokenized[i] = tokenized[i].toLowerCase();
        }
        return tokenized;
    }

    private Boolean isProductFitsKeywords(Product product, String[] keywords) {
        for (String keyword : keywords) {
            if (!(product.getTitle().toLowerCase().contains(keyword) ||
                    product.getDescription().toLowerCase().contains(keyword))) {
                return false;
            }
        }
        return true;
    }
}
