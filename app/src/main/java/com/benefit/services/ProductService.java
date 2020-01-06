package com.benefit.services;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.benefit.drivers.DatabaseDriver;
import com.benefit.model.Product;
import com.benefit.model.enums.sort.SortField;
import com.benefit.model.enums.sort.SortType;
import com.google.firebase.firestore.CollectionReference;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ProductService extends ViewModel {
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

    public MutableLiveData<Product> getProductsById(int productId) {
        return this.databaseDriver.getSingleDocumentByField(COLLECTION_NAME, "id", productId, Product.class);
    }

    public MutableLiveData<List<Product>> getProductsByCategoryId(int categoryId) {
        return this.databaseDriver.getDocumentsByField(COLLECTION_NAME, "categoryId", categoryId, Product.class);
    }

//    public MutableLiveData<List<Product>> getProductsByProperties(int categoryId, Map<String, String> propertiesMap) {
//        List<Product> filteredProducts = new LinkedList<>();
//
//        for (Map.Entry<String, String> filter : propertiesMap.entrySet()) {
//
//        }
//
//        List<Product> allProducts = getProductsByCategoryId(categoryId);
//
//        for (Product product : allProducts) {
//            boolean indicator = true;
//            for (Map.Entry<String, String> filter : propertiesMap.entrySet()) {
//                if (product.getProperties().containsKey(filter.getKey())) {
//                    if (!product.getProperties().get(filter.getKey()).contains(filter.getValue())) {
//                        indicator = false;
//                    }
//                } else {
//                    indicator = false;
//                }
//            }
//            if (indicator) {
//                filteredProducts.add(product);
//            }
//        }
//        return filteredProducts;
//    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Product> sortProducts(List<Product> products, SortField sortField, SortType sortType) {
        switch (sortField) {
            case DATE:
                products.sort(Comparator.comparing(Product::getAuctionDate));
                break;
            case LIKES:
                products.sort(Comparator.comparing(Product::getLikes));
                break;
        }
        if (sortType == SortType.DESC) {
            Collections.reverse(products);
        }
        return products;
    }
}
