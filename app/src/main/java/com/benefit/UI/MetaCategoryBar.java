package com.benefit.UI;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.benefit.MacroFiles.Category;
import com.benefit.R;
import com.benefit.StaticFunctions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetaCategoryBar {

    private View view;
    private List<Category> category;
    private Map<Category, Button> metaCategoryButtonMap;


    public MetaCategoryBar(View view, List<Category> category){
        this.view = view;
        this.category = category;
        metaCategoryButtonMap = new HashMap<>();
    }

    public void createCategoryBar(){
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

    public void createCategoryBar(Category existingMetaCategory){
        LinearLayout bar = view.findViewById(R.id.filter_bar);
        for (int i =0; i< category.size(); i ++){
            Button metaCategory = new Button(view.getContext());
            metaCategory.setText(category.get(i).getName());
            if (category.get(i).getName().equals(existingMetaCategory.getName())){
                metaCategory.setBackground(view.getResources().getDrawable(R.drawable.filled_oval));
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
    }

    public Map<Category, Button> getMetaCategoryButtonMap() {
        return metaCategoryButtonMap;
    }

}
