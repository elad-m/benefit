package com.benefit.services;

import com.benefit.drivers.DatabaseDriver;
import com.google.firebase.firestore.CollectionReference;

public class SearchService {
    private DatabaseDriver databaseDriver;
    private CollectionReference productsCollection;
    private static final String COLLECTION_NAME_PRODUCTS = "products";
    private static final String TAG = "SearchService";

    public SearchService(DatabaseDriver databaseDriver) {
        this.databaseDriver = databaseDriver;
        this.productsCollection = this.databaseDriver.getCollectionByName(COLLECTION_NAME_PRODUCTS);
    }
}
