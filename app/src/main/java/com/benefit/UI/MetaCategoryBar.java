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

    private void setBarLayoutParams(Button metaCategoryButton) {
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        int marginToSet = StaticFunctions.convertDpToPx(4);
        linearLayoutParams.setMargins(marginToSet, marginToSet, marginToSet, marginToSet);
        metaCategoryButton.setLayoutParams(linearLayoutParams);
    }


    public void createCategoryBar(List<Category> category, Category existingMetaCategory){
        this.category = category;
        LinearLayout bar = view.findViewById(R.id.filter_bar);
        for (int i =0; i< category.size(); i ++){
            Button metaCategoryButton = new Button(view.getContext());
            metaCategoryButton.setText(category.get(i).getName());
            setBackgroundColor(existingMetaCategory, metaCategoryButton, i);
            setBarLayoutParams(metaCategoryButton);
//            LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT);
//            int marginToSet = StaticFunctions.convertDpToPx(4);
//            linearLayoutParams.setMargins(marginToSet, marginToSet, marginToSet, marginToSet);
//            metaCategoryButton.setLayoutParams(linearLayoutParams);
            metaCategoryButton.setMinimumHeight(StaticFunctions.convertDpToPx(20));
            metaCategoryButton.setMinHeight(StaticFunctions.convertDpToPx(20));
            bar.addView(metaCategoryButton);
            metaCategoryButtonMap.put(category.get(i), metaCategoryButton);
        }
    }

    private void setBackgroundColor(Category existingMetaCategory, Button metaCategoryButton, int index) {
        if (existingMetaCategory != null && category.get(index).getName().equals(existingMetaCategory.getName())){
            metaCategoryButton.setBackground(view.getResources().getDrawable(R.drawable.filled_oval));
            metaCategoryButton.setTextColor(Color.WHITE);
        }
        else {
            metaCategoryButton.setBackground(view.getResources().getDrawable(R.drawable.oval));
        }
    }

    public Map<Category, Button> getMetaCategoryButtonMap() {
        return metaCategoryButtonMap;
    }

}
