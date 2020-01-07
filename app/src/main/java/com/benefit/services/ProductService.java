package com.benefit.services;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.benefit.drivers.DatabaseDriver;
import com.benefit.model.Product;
import com.benefit.model.enums.sort.SortField;
import com.benefit.model.enums.sort.SortType;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A service which responsible for products' data
 */
public class ProductService extends ViewModel {
    private DatabaseDriver databaseDriver;
    private CollectionReference productsCollection;
    private static final String COLLECTION_NAME = "products";
    private static final String TAG = "ProductService";

    public ProductService(DatabaseDriver databaseDriver) {
        this.databaseDriver = databaseDriver;
        this.productsCollection = this.databaseDriver.getCollectionReferenceByName("products");
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

    public MutableLiveData<List<Product>> getProductsBySellerId(String sellerId) {
        return this.databaseDriver.getDocumentsByField(COLLECTION_NAME, "sellerId", sellerId, Product.class);
    }

    public MutableLiveData<List<Product>> getProductsByProperties(int categoryId, Map<String, String> propertiesMap) {
        final List<Product> productsList = new LinkedList<>();
        final MutableLiveData<List<Product>> resultsLiveData = new MutableLiveData<>();
        this.databaseDriver.getCollectionReferenceByName(COLLECTION_NAME)
                .whereEqualTo("categoryId", categoryId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Product product = document.toObject(Product.class);
                            if (isPropertiesMapInProduct(product, propertiesMap)) {
                                productsList.add(product);
                            }
                        }
                        resultsLiveData.setValue(productsList);
                    }
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error on getProductsByProperties", e));
        return resultsLiveData;
    }

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

    private boolean isPropertiesMapInProduct(Product product, Map<String, String> propertiesMap) {
        if (product.getProperties() == null || product.getProperties().isEmpty()) {
            return false;
        }
        boolean indicator = true;
        for (Map.Entry<String, String> filter : propertiesMap.entrySet()) {
            if (product.getProperties() != null) {
                if (product.getProperties().containsKey(filter.getKey())) {
                    if (!product.getProperties().get(filter.getKey()).contains(filter.getValue())) {
                        indicator = false;
                    }
                } else {
                    indicator = false;
                }
            }
        }
        return indicator;
    }
}
