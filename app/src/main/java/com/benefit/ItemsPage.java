package com.benefit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.graphics.Color;
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

import com.benefit.UI.Display;
import com.benefit.drivers.DatabaseDriver;
import com.benefit.model.Category;
import com.benefit.UI.FilterPopup;
import com.benefit.UI.MetaCategoryBar;
import com.benefit.model.CategoryCluster;
import com.benefit.model.Product;
import com.benefit.model.PropertyName;
import com.benefit.services.CategoryService;
import com.benefit.services.ProductService;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ItemsPage extends AppCompatActivity {


    private static final int PRODUCTS = 2;
    private static final int CATEGORIES_DISPLAYED = 1;
    private static final int CLUSTERS_DISPLAYED = 2;
    List<PropertyName> filter;
    Map<String, List<String>> currentFilter;
    Category currentCategory;
    Category metaCategoryChosen;
    MetaCategoryBar metaCategoryBar;
    Display display;
    Map<String, String> currentFilters;
    Button metaButtonChosen;

    private DatabaseDriver databaseDriver = new DatabaseDriver();
    private CategoryService categoryService;
    private ProductService productService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentFilters = new HashMap<>();
        Bundle bundle = getIntent().getExtras();
        extractExtras(bundle);
        initiateWindow();
    }

    private void extractExtras(Bundle bundle) {
        switch (bundle.getInt("displayed")){
            case CLUSTERS_DISPLAYED:
                currentCategory = null;
                break;
            case CATEGORIES_DISPLAYED:
                currentCategory = (Category) bundle.getSerializable("category");
                if (bundle.getString("metaExists").equals("true")){
                    metaCategoryChosen = (Category) bundle.get("metaCategory");
                } else {
                    metaCategoryChosen = null;
                }
                break;
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
                writeFiltersOnScreen();
            }
        });
    }

    private void writeFiltersOnScreen() {
        LinearLayout currentFilterLayout = findViewById(R.id.current_filters);
        currentFilterLayout.removeAllViews();
        writeFilter(currentCategory.getName(), currentFilterLayout);
        if (currentFilter != null) {
            int i = 0;
            for (String filterValue : currentFilters.values()) {
                writeFilter(filterValue, currentFilterLayout);
                i++;
                if (i < currentFilters.size()) {
                    TextView orSign = new TextView(this);
                    orSign.setText(getResources().getString(R.string.divider));
                    currentFilterLayout.addView(orSign);
                }
            }
        }
    }

    private void writeFilter(String name, LinearLayout currentFilterLayout) {
        TextView filterText = new TextView(this);
        filterText.setText(name);
        LinearLayout.LayoutParams layoutParams = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(StaticFunctions.convertDpToPx(5), 0,
                StaticFunctions.convertDpToPx(5), 0);
        filterText.setLayoutParams(layoutParams);
        currentFilterLayout.addView(filterText);
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

    private void addMetaCategoryListeners() {
        for (final Map.Entry<Category, Button> metaCategory: metaCategoryBar.getMetaCategoryButtonMap().entrySet()){
            metaCategory.getValue().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (metaCategoryChosen == null){
                        instantiateAndColor(metaCategory);
//                        getChildrenOfParent();
                    } else {
                        if (!metaCategoryChosen.getName().equals(metaCategory.getKey().getName())){
                            metaButtonChosen.setBackground(getResources().getDrawable(R.drawable.oval));
                            metaButtonChosen.setTextColor(Color.BLACK);
                            instantiateAndColor(metaCategory);
//                            getChildrenOfParent();
                        } else {
                            metaButtonChosen.setBackground(getResources().getDrawable(R.drawable.oval));
                            metaButtonChosen.setTextColor(Color.BLACK);
                            metaCategoryChosen = null;
//                            getChildrenOfParent();
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
        metaButtonChosen.setTextColor(Color.WHITE);

    }

    private void initiateWindow(){
        setContentView(R.layout.activity_items_page);
        findViewById(R.id.search_icon).setBackground(getResources().getDrawable(R.drawable.ic_search_icon_color));
        findViewById(R.id.slogan).setVisibility(View.VISIBLE);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        metaCategoryBar = new MetaCategoryBar(findViewById(android.R.id.content).getRootView());
        display = new Display(findViewById(android.R.id.content).getRootView(), PRODUCTS);
        createCategoryService();
        createProductService();
        createMetaCategories();
        writeFiltersOnScreen();
    }

    private void createProductService() {
        ViewModelProvider.Factory productServiceFactory = new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new ProductService(databaseDriver);
            }
        };
        this.productService = ViewModelProviders.of(this, productServiceFactory).get(ProductService.class);
    }

    private void createCategoryService() {
        ViewModelProvider.Factory categoryServiceFactory = new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new CategoryService(databaseDriver);
            }
        };
        this.categoryService = ViewModelProviders.of(this, categoryServiceFactory).get(CategoryService.class);
    }

    private void createMetaCategories() {
        final List<Category> metaCategories = new LinkedList<>();
        final Observer<List<Category>> metaCategoriesObserver = new Observer<List<Category>>() {

            @Override
            public void onChanged(List<Category> categories) {
                metaCategories.addAll(categories);
                if (metaCategoryChosen == null) {
                    metaCategoryBar.createCategoryBar(metaCategories);
                } else {
                    metaButtonChosen = metaCategoryBar.createCategoryBar(metaCategories, metaCategoryChosen.getName());
                }
                addMetaCategoryListeners();
            }
        };
        categoryService.getAllMetaCategories().observe(this, metaCategoriesObserver);

    }

    private void getProducts(){
        final List<Product> allProducts = new LinkedList<>();
        final Observer<List<Product>> productObserver = new Observer<List<Product>>() {

            @Override
            public void onChanged(List<Product> products) {
                allProducts.addAll(products);
                display.createDisplayTable(allProducts);
//                addCategoryListeners();
            }
        };
        productService.getProductsByCategoryId(currentCategory.getId()).observe(this, productObserver);

    }

}
