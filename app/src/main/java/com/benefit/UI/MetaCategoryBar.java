package com.benefit.UI;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.benefit.MacroFiles.Category;
import com.benefit.R;

public class MetaCategoryBar {

    private View view;
    private Category category;


    public MetaCategoryBar(View view, Category category){
        this.view = view;
        this.category = category;
    }

    public void createCategoryBar(){
        LinearLayout bar = view.findViewById(R.id.filter_bar);
        for (int i =0; i< category.getNames().size(); i ++){
            Button metaCategory = new Button(view.getContext());
            metaCategory.setText(category.getNames().get(i));
            bar.addView(metaCategory);
        }
    }
}
