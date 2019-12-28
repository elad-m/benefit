package com.benefit.services;

import com.benefit.drivers.DatabaseDriver;
import com.benefit.model.Product;
import com.google.firebase.firestore.CollectionReference;

public class ProductService {
    private DatabaseDriver databaseDriver;

    public ProductService(DatabaseDriver databaseDriver) {
        this.databaseDriver = databaseDriver;
    }

    public void addProduct(Product product) {
        CollectionReference productsCollection = this.databaseDriver.getCollectionByName("products");
        productsCollection.add(product);
    }
}
