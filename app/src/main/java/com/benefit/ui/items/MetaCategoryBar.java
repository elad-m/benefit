package com.benefit.ui.items;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.benefit.R;
import com.benefit.utilities.staticclasses.Converters;
import com.benefit.model.Category;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UI for the metaCateogry bar that is on the pages
 */
public class MetaCategoryBar {

    private View view;
    private List<Category> category;
    private Map<Category, Button> metaCategoryButtonMap;


    public MetaCategoryBar(View view) {
        this.view = view;

        metaCategoryButtonMap = new HashMap<>();
    }

    private void setBarLayoutParams(Button metaCategoryButton) {
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        int marginToSet = Converters.convertDpToPx(4);
        linearLayoutParams.setMargins(marginToSet, marginToSet, marginToSet, marginToSet);
        metaCategoryButton.setLayoutParams(linearLayoutParams);
    }


    public Button createCategoryBar(List<Category> category, Category existingMetaCategory) {
        this.category = category;
        LinearLayout bar = view.findViewById(R.id.filter_bar);
        Button existingCategoryButton = null;
        for (int i = 0; i < category.size(); i++) {
            Button metaCategoryButton = new Button(view.getContext());
            metaCategoryButton.setText(category.get(i).getName());
            if (setBackgroundColor(existingMetaCategory, metaCategoryButton, i))
            {
                existingCategoryButton = metaCategoryButton;
            }
            setBarLayoutParams(metaCategoryButton);
            metaCategoryButton.setMinimumHeight(Converters.convertDpToPx(20));
            metaCategoryButton.setMinHeight(Converters.convertDpToPx(20));
            bar.addView(metaCategoryButton);
            metaCategoryButtonMap.put(category.get(i), metaCategoryButton);
        }
        return existingCategoryButton;
    }

    public void createCategoryBar(List<Category> category) {
        this.category = category;
        LinearLayout bar = view.findViewById(R.id.filter_bar);
        for (int i = 0; i < category.size(); i++) {
            Button metaCategoryButton = new Button(view.getContext());
            metaCategoryButton.setText(category.get(i).getName());
            metaCategoryButton.setBackground(view.getResources().getDrawable(R.drawable.oval));
            setBarLayoutParams(metaCategoryButton);
            metaCategoryButton.setMinimumHeight(Converters.convertDpToPx(20));
            metaCategoryButton.setMinHeight(Converters.convertDpToPx(20));
            bar.addView(metaCategoryButton);
            metaCategoryButtonMap.put(category.get(i), metaCategoryButton);
        }
    }

    private Boolean setBackgroundColor(Category existingMetaCategory, Button metaCategoryButton, int index) {
        if (existingMetaCategory != null && category.get(index).getName().equals(existingMetaCategory.getName())) {
            metaCategoryButton.setBackground(view.getResources().getDrawable(R.drawable.filled_oval));
            metaCategoryButton.setTextColor(Color.WHITE);
            return true;
        } else {
            metaCategoryButton.setBackground(view.getResources().getDrawable(R.drawable.oval));
            return false;
        }
    }

    public Map<Category, Button> getMetaCategoryButtonMap() {
        return metaCategoryButtonMap;
    }

}
