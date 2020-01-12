package com.benefit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benefit.MacroFiles.Category;
import com.benefit.MacroFiles.Filter;
import com.benefit.UI.CategoryScreen;
import com.benefit.UI.FilterPopup;
import com.benefit.UI.MetaCategoryBar;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemsPage extends AppCompatActivity {

    List<Filter> filter;
    List<Filter> currentFilter;
    String category;
    String metaCategory;
    MetaCategoryBar metaCategoryBar;
    CategoryScreen categoryScreen;
    List<String> currentFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentFilters = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        extractExtras(bundle);
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

    private void extractExtras(Bundle bundle) {
        category = bundle.getString("category");
        currentFilters.add(category);
        if (bundle.getString("metaExists").equals("true")){
            metaCategory = bundle.getString("metaCategory");
        } else {
            metaCategory = null;
        }
    }


    public void openFilter(View view) {

        RelativeLayout viewGroup =  findViewById(R.id.filter_popup);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.filter, viewGroup);

        final PopupWindow popup = new PopupWindow(this);
        int popupWidth = (int) ((getResources().getDisplayMetrics().widthPixels) / 1.2);
        int popupHeight = getResources().getDisplayMetrics().heightPixels;
        setPopupAttributes(popup, layout, popupWidth, popupHeight);

        int OFFSET_X = 0;
        int OFFSET_Y = 0;

        popup.showAtLocation(layout, Gravity.END, OFFSET_X, OFFSET_Y);
        FilterPopup filterPopup = new FilterPopup(layout, filter);
        filterPopup.populateFilter(currentFilters);

        setPopupOnClickListeners(layout, popup, filterPopup);
    }

    private void setPopupAttributes(PopupWindow popup, View layout, int width, int height) {


        popup.setContentView(layout);
        popup.setWidth(width);
        popup.setHeight(height);
        popup.setFocusable(true);
    }

    private void setPopupOnClickListeners(View layout, final PopupWindow popup, final FilterPopup filterPopup) {
        Button reset =  layout.findViewById(R.id.reset_button);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterPopup.refresh();
            }
        });

        Button done = layout.findViewById(R.id.done_button);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFilters = filterPopup.getCurrentFilters();
                popup.dismiss();
                reWriteFiltersOnScreen();
            }
        });
    }

    private void reWriteFiltersOnScreen() {
        LinearLayout currentFilterLayout = findViewById(R.id.current_filters);
        currentFilterLayout.removeAllViews();
        writeFiltersOnScreen();
    }

    private void writeFiltersOnScreen() {
        LinearLayout currentFilterLayout = findViewById(R.id.current_filters);
        int i = 0;
        while (i < currentFilters.size()){
            TextView filterText = new TextView(this);
            filterText.setText(currentFilters.get(i));
            LinearLayout.LayoutParams layoutParams = new
                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(StaticFunctions.convertDpToPx(5), 0,
                    StaticFunctions.convertDpToPx(5), 0);
            filterText.setLayoutParams(layoutParams);
            currentFilterLayout.addView(filterText);
            if (i < currentFilters.size() - 1){
                TextView orSign = new TextView(this);
                orSign.setText(getResources().getString(R.string.divider));
                currentFilterLayout.addView(orSign);
            }
            i++;

        }
    }

    public void openSort(View view) {
        int popupWidth = StaticFunctions.convertDpToPx(150);
        int popupHeight = StaticFunctions.convertDpToPx(200);
        LinearLayout viewGroup =  findViewById(R.id.sort_popup);
        int[] location = new int[2];
        findViewById(R.id.sort).getLocationOnScreen(location);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.sort, viewGroup);

        final PopupWindow popup = new PopupWindow(this);
        setPopupAttributes(popup, layout, popupWidth, popupHeight);
        int OFFSET_X = location[0];
        int OFFSET_Y = location[1] +  StaticFunctions.convertDpToPx(50);

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
        if (metaCategory != null){
            metaCategoryBar.createCategoryBar(metaCategory);
        } else {
            metaCategoryBar.createCategoryBar();
        }
        categoryScreen = new CategoryScreen(findViewById(android.R.id.content).getRootView());
        categoryScreen.createCategoryTable(ct);
        writeFiltersOnScreen();
    }
}
