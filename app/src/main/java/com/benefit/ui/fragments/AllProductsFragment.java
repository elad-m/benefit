package com.benefit.ui.fragments;


import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.benefit.R;
import com.benefit.activities.MainActivity2;
import com.benefit.model.Category;
import com.benefit.model.CategoryCluster;
import com.benefit.model.Product;
import com.benefit.model.PropertyName;
import com.benefit.services.CategoryService;
import com.benefit.services.ProductService;
import com.benefit.services.SearchService;
import com.benefit.ui.Displayable;
import com.benefit.ui.products.FilterPopup;
import com.benefit.ui.products.ProductsPageUI;
import com.benefit.utilities.Factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllProductsFragment extends Fragment {

    private static final int ONE_CATEGORY_DISPLAYED = 1;
    private static final int MULTIPLE_CATEGORIES_DISPLAYED = 2;
    private static final int CATEGORY_PRODUCTS = 1;
    private static final int FILTERED_PRODUCTS = 2;
    private static final int ALL_CATEGORY_SEARCH = 1;
    private static final int SPECIFIC_CATEGORY_SEARCH = 2;

    public static AllProductsFragment newInstance(Boolean fromSearch, String searchText,
                                                  int itemsDisplayed, Displayable displayable,
                                                  Category metaCategoryChosen){
        AllProductsFragment fragment = new AllProductsFragment();
        if (fromSearch){
            fragment.searchExecuted = fromSearch;
            fragment.searchText = searchText;
        }
        else {
            fragment.multiOrSingleCategory = itemsDisplayed;
            fragment.metaCategoryChosen = metaCategoryChosen;
            switch (itemsDisplayed){
                case MULTIPLE_CATEGORIES_DISPLAYED:
                    fragment.currentCategory = null;
                    fragment.categoryCluster = (CategoryCluster) displayable;
                    break;
                case ONE_CATEGORY_DISPLAYED:
                    fragment.currentCategory = (Category) displayable;
                    fragment.categoryCluster = null;
            }
        }
        return fragment;
    }

    private boolean searchExecuted = false;
    private int multiOrSingleCategory;
    private Category currentCategory;
    private Category metaCategoryChosen;
    private CategoryCluster categoryCluster;
    private String searchText;
    private List<PropertyName> allFilters;
    private Map<String, List<String>> currentFilters;
    private ProductsPageUI pageUI;
    private CategoryService categoryService;
    private ProductService productService;
    private SearchService searchService;
    private View allProductsView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_products, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        allProductsView = view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        currentFilters = new HashMap<>();
        allFilters = new ArrayList<>();
        initiateServices();
        if(savedInstanceState != null){
            extractBundle(savedInstanceState);
        }
        initiateWindow();
        addSearchListener();
    }

    private void initiateServices(){
        categoryService = ViewModelProviders.of(this, Factory.getCategoryServiceFactory()).get(CategoryService.class);
        productService = ViewModelProviders.of(this, Factory.getProductServiceFactory()).get(ProductService.class);
        searchService = ViewModelProviders.of(this, Factory.getSearchServiceFactory()).get(SearchService.class);
    }

    private void extractBundle(Bundle savedInstanceState){
        searchExecuted = savedInstanceState.getBoolean(getResources().getString(R.string.received_search));
        multiOrSingleCategory = savedInstanceState.getInt(getResources().getString(R.string.displayed));
        currentCategory = (Category) savedInstanceState.getSerializable(getResources().getString(R.string.category));
        categoryCluster = (CategoryCluster) savedInstanceState.getSerializable(getResources().getString(R.string.cluster));
        metaCategoryChosen = (Category) savedInstanceState.getSerializable(getResources().getString(R.string.meta_category));
        searchText = savedInstanceState.getString(getResources().getString(R.string.search));
    }

    private void search(int whatSearched, String searchText) {
        final Observer<List<Product>> searchObserver = products -> {
            pageUI.addDisplayTable(products);
            addProductListeners();
        };
        searchInCategory(whatSearched, searchText, searchObserver);
    }

    private void searchInCategory(int whatSearched, String searchText, Observer<List<Product>> searchObserver) {
        switch (whatSearched) {
            case ALL_CATEGORY_SEARCH:
                searchService.getProductsBySearchString(searchText).observe(this, searchObserver);
                break;
            case SPECIFIC_CATEGORY_SEARCH:
                switch (multiOrSingleCategory) {
                    case MULTIPLE_CATEGORIES_DISPLAYED:
                        for (int categoryId : categoryCluster.getCategoryIdList()) {
                            searchService.getProductsBySearchString(searchText, categoryId).
                                    observe(this, searchObserver);
                        }
                        break;
                    case ONE_CATEGORY_DISPLAYED:
                        searchService.getProductsBySearchString(searchText, currentCategory.getIdAsInt()).
                                observe(this, searchObserver);
                        break;
                }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initiateWindow() {
        setWindow();
        pageUI = new ProductsPageUI(allProductsView, currentCategory, categoryCluster,
                getActivity().findViewById(R.id.header));
        getAllMetaCategories();
        addFilterListener();
        if (searchExecuted) {
            search(ALL_CATEGORY_SEARCH, searchText);
        }
        else {
            addProductsToScreen();
            if (multiOrSingleCategory == ONE_CATEGORY_DISPLAYED) {
                getFilters(currentCategory.getIdAsInt());
            } else {
                getAllCategoryFilters();
            }
            pageUI.writeFiltersOnScreen(currentFilters);
        }
    }

    private void setWindow(){
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }

    private void getAllMetaCategories() {
        final Observer<List<Category>> metaCategoriesObserver = categories -> {
            if (metaCategoryChosen == null) {
                pageUI.addMetaCategoryBar(categories);
            } else {
                pageUI.addMetaCategoryBar(categories, metaCategoryChosen);
            }
            addMetaCategoryListeners();
        };
        categoryService.getAllMetaCategories().observe(this, metaCategoriesObserver);
    }

    private void addMetaCategoryListeners() {
        for (final Map.Entry<Category, Button> metaCategory : pageUI.getMetaCategoryButtonMap().entrySet()) {
            metaCategory.getValue().setOnClickListener(v -> {
                if (metaCategoryChosen != null && metaCategoryChosen.getName().equals(metaCategory.getKey().getName())) {
                    ((MainActivity2) getActivity()).startHomeFragment(null);
                } else {
                    ((MainActivity2) getActivity()).startHomeFragment(metaCategory.getKey());
                }
            });
        }
    }

    private void addProductsToScreen() {
        switch (multiOrSingleCategory) {
            case MULTIPLE_CATEGORIES_DISPLAYED:
                for (int categoryId : categoryCluster.getCategoryIdList()) {
                    showProducts(CATEGORY_PRODUCTS, categoryId);
                }
                break;
            case ONE_CATEGORY_DISPLAYED:
                showProducts(CATEGORY_PRODUCTS, currentCategory.getIdAsInt());
                break;
        }
    }

    private void showProducts(int productsToShow, int categoryId) {
        final Observer<List<Product>> productObserver = products -> {
            pageUI.addDisplayTable(products);
            addProductListeners();
        };
        switch (productsToShow) {
            case CATEGORY_PRODUCTS:
                productService.getProductsByCategoryId(categoryId).observe(this, productObserver);
                break;
            case FILTERED_PRODUCTS:
                productService.getProductsByProperties(categoryId, currentFilters).observe(this, productObserver);
                break;

        }
    }

    private void addProductListeners() {
        pageUI.getDisplayableAdapter().setOnItemClickListener(position -> {
            Displayable productClicked = pageUI.getDisplayableProducts().get(position);
            ((MainActivity2) getActivity()).startProductFragment((Product) productClicked);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getFilters(int categoryId) {
        final Observer<List<PropertyName>> propertiesObserver = properties -> {
            for (PropertyName propertyName : properties) {
                if (propertyName.getValidValues() != null &&
                        propertyNameNotInAllFilters(propertyName)) {
                    allFilters.add(propertyName);
                }
            }
        };
        categoryService.getAllPropertiesByCategoryId(categoryId).observe(this, propertiesObserver);
    }

    private boolean propertyNameNotInAllFilters(PropertyName propertyName) {
        for (PropertyName filter : allFilters) {
            if (filter.getName().equals(propertyName.getName())) {
                return false;
            }
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getAllCategoryFilters() {
        for (int categoryId : categoryCluster.getCategoryIdList()) {
            getFilters(categoryId);
        }
    }

    private void addFilterListener(){
        TextView filterButton = allProductsView.findViewById(R.id.filter);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageUI.openFilter(view, allFilters, currentFilters);
                setFilterOnClickListeners(pageUI.getPopupView(), pageUI.getPopup(), pageUI.getFilterPopup());
            }
        });
    }

    private void setFilterOnClickListeners(View layout, final PopupWindow popup, final FilterPopup filterPopup) {
        Button reset = layout.findViewById(R.id.reset_button);
        reset.setOnClickListener(v -> filterPopup.refreshFilter());

        Button done = layout.findViewById(R.id.done_button);
        done.setOnClickListener(v -> {
            currentFilters = filterPopup.getCurrentFilters();
            popup.dismiss();
            if (!searchExecuted) {
                pageUI.writeFiltersOnScreen(currentFilters);
                pageUI.refreshTable();
                if (currentFilters.size() == 0) {
                    addProductsToScreen();
                } else {
                    showAllFilteredProducts();
                }
            }
        });
    }

    private void showAllFilteredProducts() {
        switch (multiOrSingleCategory) {
            case MULTIPLE_CATEGORIES_DISPLAYED:
                for (int id : categoryCluster.getCategoryIdList()) {
                    showProducts(FILTERED_PRODUCTS, id);
                }
                break;
            case ONE_CATEGORY_DISPLAYED:
                showProducts(FILTERED_PRODUCTS, currentCategory.getIdAsInt());
                break;
        }
    }

    private void addSearchListener() {
        SearchView searchView = allProductsView.findViewById(R.id.search_input);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() >= 1) {
                    pageUI.refreshTable();
                    search(SPECIFIC_CATEGORY_SEARCH, query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(getResources().getString(R.string.received_search), searchExecuted);
        outState.putInt(getResources().getString(R.string.displayed), multiOrSingleCategory);
        outState.putSerializable(getResources().getString(R.string.category), currentCategory);
        outState.putSerializable(getResources().getString(R.string.cluster), categoryCluster);
        outState.putSerializable(getResources().getString(R.string.meta_category), metaCategoryChosen);
        outState.putString(getResources().getString(R.string.search), searchText);
    }
}
