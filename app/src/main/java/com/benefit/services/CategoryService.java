package com.benefit.services;

import com.benefit.drivers.DatabaseDriver;
import com.benefit.model.Category;
import com.google.firebase.firestore.CollectionReference;

public class CategoryService {
    private DatabaseDriver databaseDriver;

    public CategoryService(DatabaseDriver databaseDriver) {
        this.databaseDriver = databaseDriver;
    }

    public void addCategory(Category category) {
        CollectionReference categoriesCollection = this.databaseDriver.getCollectionByName("categories");
        categoriesCollection.add(category);
    }
}
