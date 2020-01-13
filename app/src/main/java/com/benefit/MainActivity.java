package com.benefit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.benefit.MacroFiles.Category;
import com.benefit.UI.CategoryScreen;
import com.benefit.UI.MetaCategoryBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.benefit.drivers.DatabaseDriver;
import com.benefit.model.Category;
import com.benefit.services.CategoryService;

public class MainActivity extends AppCompatActivity {

    CategoryScreen categoryScreen;
    MetaCategoryBar metaCategoryBar;
    Category metaCategoryChosen;
    Button metaButtonChosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initiateWindow();
        addOnClickListeners();
    }

    private void addOnClickListeners() {
        addMetaCategoryListeners();
        addCategoryListeners();
    }

    private void addCategoryListeners() {
        for (final Map.Entry<Category, FrameLayout> categoryAndButton: categoryScreen.getCategoryButtonMap().entrySet()){
            categoryAndButton.getValue().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ItemsPage.class);
                    intent.putExtra("category", categoryAndButton.getKey().getName());
                    if (metaCategoryChosen == null){
                        intent.putExtra("metaExists", "false");
                    } else {
                        intent.putExtra("metaExists", "true");
                        intent.putExtra("metaCategory", metaCategoryChosen.getName());
                    }
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    private void addMetaCategoryListeners() {
        for (final Map.Entry<Category, Button> metaCategory: metaCategoryBar.getMetaCategoryButtonMap().entrySet()){
            metaCategory.getValue().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (metaCategoryChosen == null){
                        instantiateAndColor(metaCategory);

//                        categoryScreen.refreshItems();
                    } else {
                        if (!metaCategoryChosen.getName().equals(metaCategory.getKey().getName())){
                            metaButtonChosen.setBackground(getResources().getDrawable(R.drawable.oval));
                            instantiateAndColor(metaCategory);

//                            categoryScreen.refreshItems();
                        } else {
                            metaButtonChosen.setBackground(getResources().getDrawable(R.drawable.oval));
                            metaCategoryChosen = null;

//                            categoryScreen.refreshItems();
                        }
                    }
                }
            });
        }
    }

    private void instantiateAndColor(Map.Entry<Category, Button> metaCategory) {
        metaCategoryChosen = metaCategory.getKey();
        metaButtonChosen = metaCategory.getValue();
        metaButtonChosen.setBackground(getResources().getDrawable(R.drawable.filled_oval));

    }

    private void initiateWindow(){
        setContentView(R.layout.activity_main);
        findViewById(R.id.search_icon).setBackground(getResources().getDrawable(R.drawable.ic_search_icon_color));
        findViewById(R.id.slogan).setVisibility(View.VISIBLE);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        List<Category> ct = IO.getDatabaseFromInputStream(getResources().openRawResource(R.raw.database));
        List<String> list1 = new ArrayList<>();
        list1.add("men");
        list1.add("women");
        list1.add("kids");
        list1.add("teens");
        list1.add("toddlers");
        List<Category> metaList = new ArrayList<>();
        for (String name:list1){
            Category category = new Category();
            category.setName(name);
            metaList.add(category);
        }
        metaCategoryBar = new MetaCategoryBar(findViewById(android.R.id.content).getRootView(), metaList);
        metaCategoryBar.createCategoryBar();
        categoryScreen = new CategoryScreen(findViewById(android.R.id.content).getRootView());
        categoryScreen.createCategoryTable(ct);

    }
}

}