package com.benefit;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.benefit.model.Product;

public class ProductPageActivity extends AppCompatActivity {

    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extractExtras();
        setContentView(R.layout.activity_product_page);
        findViewById(R.id.search_icon).setBackground(getResources().getDrawable(R.drawable.ic_search_icon_color));


    }

    private void extractExtras() {
//        Bundle bundle = getIntent().getExtras();
//        product = (Product) bundle.getSerializable("product");
    }


}
