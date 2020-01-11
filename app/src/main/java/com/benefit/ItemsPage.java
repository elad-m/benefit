package com.benefit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benefit.MacroFiles.Category;
import com.benefit.MacroFiles.Filter;
import com.benefit.UI.CategoryScreen;
import com.benefit.UI.FilterPopup;
import com.benefit.UI.MetaCategoryBar;

import java.util.ArrayList;
import java.util.List;

public class ItemsPage extends AppCompatActivity {

    List<Filter> filter;
    List<Filter> currentFilter;
    MetaCategoryBar metaCategoryBar;
    CategoryScreen categoryScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        initiateWindow();
        List<String> option1 = new ArrayList<>();
        option1.add("large");
        option1.add("Xlarge");
        option1.add("small");
        option1.add("medium");
        List<String> option2 = new ArrayList<>();
        option2.add("winter");
        option2.add("summer");
        option2.add("fall");
        option2.add("spring");
        filter = new ArrayList<>();
        currentFilter = new ArrayList<>();
        filter.add(new Filter("Size", option1));
        filter.add(new Filter("Season", option2));
        filter.add(new Filter("Size", option1));
        filter.add(new Filter("Season", option2));
    }


    public void openFilter(View view) {
        int popupWidth = (int) ((getResources().getDisplayMetrics().widthPixels) / 1.2);
        int popupHeight = (int) (getResources().getDisplayMetrics().heightPixels);

        LinearLayout viewGroup =  findViewById(R.id.filter_popup);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.filter, viewGroup);

        final PopupWindow popup = new PopupWindow(this);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);

        int OFFSET_X = 0;
        int OFFSET_Y = 0;

        popup.showAtLocation(layout, Gravity.END, OFFSET_X, OFFSET_Y);
        FilterPopup filterPopup = new FilterPopup(layout, filter);
        filterPopup.populateFilter();

        Button reset =  layout.findViewById(R.id.reset_button);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });

        Button done = layout.findViewById(R.id.done_button);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
    }

    public void openSort(View view) {
        int popupWidth = StaticFunctions.convertDpToPx(150);
        int popupHeight = StaticFunctions.convertDpToPx(200);
        // Inflate the popup_layout.xml
        LinearLayout viewGroup =  findViewById(R.id.sort_popup);
        int[] location = new int[2];
        findViewById(R.id.sort).getLocationOnScreen(location);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.sort, viewGroup);
        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(this);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);
        // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
        int OFFSET_X = location[0];
        int OFFSET_Y = location[1] +  StaticFunctions.convertDpToPx(50);
        // Clear the default translucent background
//        popup.setBackground (new BitmapDrawable());
        // Displaying the popup at the specified location, + offsets.
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, OFFSET_X, OFFSET_Y);

    }

    private void initiateWindow(){
        setContentView(R.layout.activity_items_page);
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
