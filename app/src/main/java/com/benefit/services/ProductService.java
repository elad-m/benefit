package com.benefit.services;

import com.benefit.drivers.DatabaseDriver;
import com.benefit.model.Product;
import com.google.firebase.firestore.CollectionReference;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    public List<Product> getProductsByProperties(int categoryId, Map<String, String> propertiesMap) {
        List<Product> allProducts = getProductsByCategoryId(categoryId);
        List<Product> filteredProducts = new LinkedList<>();
        for (Product product : allProducts) {
            boolean indicator = true;
            for (Map.Entry<String, String> filter : propertiesMap.entrySet()) {
                if (product.getProperties().containsKey(filter.getKey())) {
                    if (!product.getProperties().get(filter.getKey()).contains(filter.getValue())) {
                        indicator = false;
                    }
                } else {
                    indicator = false;
                }
            }
            if (indicator) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }
}
