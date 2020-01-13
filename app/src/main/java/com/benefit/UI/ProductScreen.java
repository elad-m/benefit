package com.benefit.UI;

import android.view.View;
import android.widget.TextView;

import com.benefit.R;
import com.benefit.model.Product;

import java.util.List;
import java.util.Map;

public class ProductScreen {

    private final View view;
    private Product product;

    public ProductScreen(View view, Product product){
        this.view = view;
        this.product = product;
        createProductScreen();
    }

    private void createProductScreen() {
        addInfo();
    }

    private void addInfo() {
        Map<String, List<String>> properties = product.getProperties();
        for (Map.Entry<String, List<String>> entry: properties.entrySet())
            switch (entry.getKey()){
                case "gender":
                    TextView gender = view.findViewById(R.id.gender);
                    gender.setText(entry.getValue().get(0));
                    break;
                case "size":
                    TextView size = view.findViewById(R.id.size);
                    String sizeText = "Size: " + entry.getValue();
                    size.setText(sizeText);
                    break;
                case "color":
                    TextView color = view.findViewById(R.id.color);
                    String colorText = "Color: " + entry.getValue();
                    color.setText(colorText);
                    break;
                case "condition":
                    TextView condition = view.findViewById(R.id.condition);
                    String conditionText = "Condition: " + entry.getValue();
                    condition.setText(conditionText);
                    break;
            }

    }
}
