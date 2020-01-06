package com.benefit.services;

import androidx.annotation.NonNull;

import com.benefit.drivers.DatabaseDriver;
import com.benefit.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ProductService {
    private DatabaseDriver databaseDriver;
    private CollectionReference productsCollectionRef;

    public ProductService(DatabaseDriver databaseDriver) {
        this.databaseDriver = databaseDriver;
        this.productsCollectionRef = this.databaseDriver.getCollectionReferenceByName("products");
    }

    public void addProduct(Product product) {
        this.productsCollectionRef.add(product);
    }

    public Product getProductsById(int productId) {
        final List<Product> productList = new LinkedList<>();
        productsCollectionRef
                .whereEqualTo("id", productId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot productDocument : Objects.requireNonNull(task.getResult())) {
                                productList.add(productDocument.toObject(Product.class));
                            }
                        }
                    }
                });
        return productList.get(0);
    }

    public List<Product> getProductsByCategoryId(int categoryId) {
        final List<Product> productList = new LinkedList<>();
        productsCollectionRef
                .whereEqualTo("categoryId", categoryId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot productDocument : Objects.requireNonNull(task.getResult())) {
                                productList.add(productDocument.toObject(Product.class));
                            }
                        }
                    }
                });
        return productList;
    }
}
