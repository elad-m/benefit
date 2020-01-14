package com.benefit.UI;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.benefit.R;
import com.benefit.StaticFunctions;
import com.benefit.model.Category;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetaCategoryBar {

    private View view;
    private List<Category> category;
    private Map<Category, Button> metaCategoryButtonMap;


    public MetaCategoryBar(View view){
        this.view = view;

        metaCategoryButtonMap = new HashMap<>();
    }

    public void createCategoryBar(List<Category> category){
        this.category = category;
        LinearLayout bar = view.findViewById(R.id.filter_bar);
        for (int i =0; i< category.size(); i ++){
            Button metaCategory = new Button(view.getContext());
            metaCategory.setText(category.get(i).getName());
            metaCategory.setBackground(view.getResources().getDrawable(R.drawable.oval));
            LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            int marginToSet = StaticFunctions.convertDpToPx(4);
            linearLayoutParams.setMargins(marginToSet, marginToSet, marginToSet, marginToSet);
            metaCategory.setLayoutParams(linearLayoutParams);
            metaCategory.setMinimumHeight(StaticFunctions.convertDpToPx(25));
            metaCategory.setMinHeight(StaticFunctions.convertDpToPx(25));
            bar.addView(metaCategory);
            metaCategoryButtonMap.put(category.get(i), metaCategory);
        }
    }

    public Button createCategoryBar(List<Category> category, String existingMetaCategory){
        Button chosenButton = null;
        this.category = category;
        LinearLayout bar = view.findViewById(R.id.filter_bar);
        for (int i =0; i< category.size(); i ++){
            Button metaCategory = new Button(view.getContext());
            metaCategory.setText(category.get(i).getName());
            if (category.get(i).getName().equals(existingMetaCategory)){
                metaCategory.setBackground(view.getResources().getDrawable(R.drawable.filled_oval));
                metaCategory.setTextColor(Color.WHITE);
                chosenButton = metaCategory;
            }
            else {
                metaCategory.setBackground(view.getResources().getDrawable(R.drawable.oval));
            }
            LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            int marginToSet = StaticFunctions.convertDpToPx(4);
            linearLayoutParams.setMargins(marginToSet, marginToSet, marginToSet, marginToSet);
            metaCategory.setLayoutParams(linearLayoutParams);
            metaCategory.setMinimumHeight(StaticFunctions.convertDpToPx(25));
            metaCategory.setMinHeight(StaticFunctions.convertDpToPx(25));
            bar.addView(metaCategory);
            metaCategoryButtonMap.put(category.get(i), metaCategory);
        }
        return chosenButton;
    }

    public Map<Category, Button> getMetaCategoryButtonMap() {
        return metaCategoryButtonMap;
    }

}
