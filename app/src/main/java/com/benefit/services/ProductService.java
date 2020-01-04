package com.benefit.services;

import com.benefit.drivers.DatabaseDriver;
import com.benefit.model.Product;
import com.google.firebase.firestore.CollectionReference;

import java.util.List;

public class ProductService {
    private DatabaseDriver databaseDriver;
    private CollectionReference productsCollection;
    private static final String COLLECTION_NAME = "products";

    public ProductService(DatabaseDriver databaseDriver) {
        this.databaseDriver = databaseDriver;
        this.productsCollection = this.databaseDriver.getCollectionByName("products");
    }

    public void addProduct(Product product) {
        this.productsCollection.add(product);
    }

    public Product getProductsById(int productId) {
        List<Product> productList = this.databaseDriver.getDocumentsByField(
                COLLECTION_NAME, "id", productId, Product.class);
        return productList.get(0);
    }

    public List<Product> getProductsByCategoryId(int categoryId) {
        return this.databaseDriver.getDocumentsByField(COLLECTION_NAME, "categoryId", categoryId, Product.class);
    }
}
