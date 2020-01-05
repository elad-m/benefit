package com.benefit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.benefit.MacroFiles.Category;
import com.benefit.UI.CategoryScreen;
import com.benefit.UI.MetaCategoryBar;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.benefit.drivers.DatabaseDriver;
import com.benefit.model.Category;
import com.benefit.services.CategoryService;

public class MainActivity extends AppCompatActivity {

//    List<String> categoryNames;
//    List<String> categoryImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Category ct = IO.getDatabaseFromInputStream(getResources().openRawResource(R.raw.database));
//        categoryNames = ct.getNames();
//        categoryImages = ct.getImages();
//        MetaCategoryBar metaCategoryBar = new MetaCategoryBar(findViewById(android.R.id.content).getRootView(), ct);
//        metaCategoryBar.createCategoryBar();
        CategoryScreen categoryScreen = new CategoryScreen(findViewById(android.R.id.content).getRootView(), ct);
        categoryScreen.createCategoryTable();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

//    private void createCategoryTable() {
//        TableLayout itemTable = findViewById(R.id.item_table);
//        int numberOfRows = getNumberOfRows();
//        for (int i = 0; i < numberOfRows; i++){
//            addRow(itemTable, i);
//        }
//
//    }
//
//    private void addRow(TableLayout itemTable, int index) {
//        TableRow tableRow = new TableRow(this);
//        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
//                TableLayout.LayoutParams.MATCH_PARENT);
//        lp.bottomMargin = StaticFunctions.convertDpToPx(10);
//        lp.topMargin = StaticFunctions.convertDpToPx(10);
//        lp.gravity = Gravity.CENTER;
//        lp.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
//        tableRow.setLayoutParams(lp);
//        for (int i = 0; i < 2; i++){
//            if (2 * index + i < categoryNames.size() - 1) {
//                addCategory(tableRow, 2 * index + i);
//            }
//        }
//        itemTable.addView(tableRow);
//
//    }
//
//    private void addCategory(TableRow tableRow, int index) {
//        FrameLayout category = new FrameLayout(this);
//        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
//                TableRow.LayoutParams.WRAP_CONTENT);
//        lp.leftMargin = StaticFunctions.convertDpToPx(50);
//        lp.rightMargin = StaticFunctions.convertDpToPx(50);
//        lp.height = StaticFunctions.convertDpToPx(80);
//        lp.width = StaticFunctions.convertDpToPx(80);
//        category.setLayoutParams(lp);
//        category.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), items_page.class);
//                startActivity(intent);
//            }
//
//        });
////        addImage(category, index);
//        addText(category, index);
//        tableRow.addView(category);
//    }
//
//    private void addText(FrameLayout category, int index) {
//        TextView text = new TextView(this);
//        text.setText(categoryNames.get(index));
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.WRAP_CONTENT);
//        lp.gravity = Gravity.BOTTOM;
//        text.setLayoutParams(lp);
//        category.addView(text);
//    }
//
//    private void addImage(FrameLayout category, int index) {
//        ImageView image = new ImageView(this);
//        image.setImageDrawable(convertStringToDrawable(categoryImages.get(index)));
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.WRAP_CONTENT);
//        lp.bottomMargin = StaticFunctions.convertDpToPx(20);
//        image.setLayoutParams(lp);
//        category.addView(image);
//
//
//    }
//
//    private Drawable convertStringToDrawable(String name) {
//        int id = getResources().getIdentifier(name, "drawable",
//                getPackageName());
//        return getResources().getDrawable(id);
//    }
//
//    private int getNumberOfRows() {
//        if (categoryNames.size() % 2 == 0){
//            return categoryNames.size() / 2;
//        } else{
//            return categoryNames.size() / 2  + 1;
//        }
//    }




    }

}