package com.benefit;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.benefit.model.Product;
import com.benefit.services.Factory;
import com.benefit.services.ProductService;

import java.util.List;
import java.util.Map;

public class ProductPageActivity extends AppCompatActivity {

    private int productId = 1;
    private ProductService productService;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ProductPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);
        this.productService = ViewModelProviders.of(this, Factory.getProductServiceFactory()).get(ProductService.class);
        final Observer<Product> productObserver = this::displayProductOnPage;
        productService.getProductById(this.productId).observe(this, productObserver);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void displayProductOnPage(Product product) {
        TextView textViewTitle = findViewById(R.id.textviewProductPageTitle);
        TextView textViewDescription = findViewById(R.id.textviewProductPageDescription);
        TextView textViewProperties = findViewById(R.id.textviewProductPageProperties);
        textViewTitle.setText(product.getTitle());
        textViewDescription.setText(product.getDescription());
        textViewProperties.setText(getPropertiesString(product.getProperties()));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getPropertiesString(Map<String, List<String>> propertiesMap) {
        StringBuilder propertiesString = new StringBuilder();
        for (String name : propertiesMap.keySet()) {
            propertiesString
                    .append(name)
                    .append(":")
                    .append(" ")
                    .append(String.join(getString(R.string.productPagePropertiesValuesSeparator) + " ", propertiesMap.get(name)));
            propertiesString
                    .append(" ")
                    .append(getString(R.string.productPagePropertiesSeparator))
                    .append(" ");
        }
        return propertiesString.toString();
    }
}
