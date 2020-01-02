package com.benefit.UI;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaCodecInfo;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.benefit.MacroFiles.Category;
import com.benefit.R;
import com.benefit.StaticFunctions;
import com.benefit.items_page;

public class CategoryScreen {

    private View view;
    private Category category;
//    Product product;

    public CategoryScreen(View view, Category category){
        this.view = view;
        this.category = category;
//        this.product = product;
    }

    public void createCategoryTable() {
        TableLayout itemTable = view.findViewById(R.id.item_table);
        int numberOfRows = getNumberOfRows();
        for (int i = 0; i < numberOfRows; i++){
            addRow(itemTable, i);
        }

    }

    private void addRow(TableLayout itemTable, int index) {
        TableRow tableRow = new TableRow(view.getContext());
        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT);
        lp.bottomMargin = StaticFunctions.convertDpToPx(10);
        lp.topMargin = StaticFunctions.convertDpToPx(10);
        lp.gravity = Gravity.CENTER;
        lp.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        tableRow.setLayoutParams(lp);
        for (int i = 0; i < 2; i++){
            if (2 * index + i < category.getNames().size()) {
                addCategory(tableRow, 2 * index + i);
            }
        }
        itemTable.addView(tableRow);

    }

    private void addCategory(TableRow tableRow, int index) {
        FrameLayout category = new FrameLayout(view.getContext());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = StaticFunctions.convertDpToPx(50);
        lp.rightMargin = StaticFunctions.convertDpToPx(50);
        lp.height = StaticFunctions.convertDpToPx(80);
        lp.width = StaticFunctions.convertDpToPx(80);
        category.setLayoutParams(lp);
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), items_page.class);
                view.getContext().startActivity(intent);
            }

        });
        addImage(category, index);
        addText(category, index);
        tableRow.addView(category);
    }

    private void addText(FrameLayout fl, int index) {
        TextView text = new TextView(view.getContext());
        text.setText(category.getNames().get(index));
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM;
        lp.gravity = Gravity.CENTER;
        text.setLayoutParams(lp);
        fl.addView(text);
    }

    private void addImage(FrameLayout fl, int index) {
        ImageView image = new ImageView(view.getContext());
//        image.setImageDrawable(convertStringToDrawable(category.getNames().get(index)));
        image.setBackgroundColor(Color.BLUE);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = StaticFunctions.convertDpToPx(20);
        image.setLayoutParams(lp);
        fl.addView(image);


    }

    private Drawable convertStringToDrawable(String name) {
        int id = view.getResources().getIdentifier(name, "drawable",
                view.getContext().getPackageName());
        return view.getResources().getDrawable(id);
    }

    private int getNumberOfRows() {
        if (category.getNames().size() % 2 == 0){
            return category.getNames().size() / 2;
        } else{
            return category.getNames().size() / 2  + 1;
        }
    }

}
