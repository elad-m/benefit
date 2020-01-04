package com.benefit.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.benefit.drivers.DatabaseDriver;
import com.benefit.model.Category;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class CategoryService {
    private DatabaseDriver databaseDriver;
    private CollectionReference categoriesCollection;
    private static final String TAG = "CategoryService";

    public CategoryService(DatabaseDriver databaseDriver) {
        this.databaseDriver = databaseDriver;
        this.categoriesCollection = this.databaseDriver.getCollectionByName("categories");
    }

    public void addCategory(Category category) {
        this.categoriesCollection.add(category);
    }

    public Category getCategoryById(int categoryId) {
        final List<Category> categoriesList = new LinkedList<>();
        categoriesCollection
                .whereEqualTo("id", categoryId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot categoryDocument : Objects.requireNonNull(task.getResult())) {
                                categoriesList.add(categoryDocument.toObject(Category.class));
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error on getCategoryById", e);
                    }
                });
        return categoriesList.get(0);
    }

    public List<Category> getCategorisByField(String fieldName, String fieldValue) {
        final List<Category> categoriesList = new LinkedList<>();
        categoriesCollection
                .whereEqualTo(fieldName, fieldValue)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot categoryDocument : Objects.requireNonNull(task.getResult())) {
                                categoriesList.add(categoryDocument.toObject(Category.class));
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error on getCategorisByField", e);
                    }
                });
        return categoriesList;
    }
}
