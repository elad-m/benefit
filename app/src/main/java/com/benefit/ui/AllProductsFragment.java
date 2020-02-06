package com.benefit.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.benefit.R;
import com.benefit.model.Category;
import com.benefit.model.CategoryCluster;
import com.benefit.model.PropertyName;
import com.benefit.services.CategoryService;
import com.benefit.services.ProductService;
import com.benefit.services.SearchService;
import com.benefit.ui.products.ProductsPageUI;
import com.benefit.utilities.Factory;

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
            fragment.are_products_displayed = true;
            fragment.searchText = searchText;
        }
        else {
            fragment.whichProductsDisplayed = itemsDisplayed;
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

    private boolean are_products_displayed = false;
    private int whichProductsDisplayed;
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.all_products_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initiateServices();
        if(savedInstanceState != null){
            extractBundle(savedInstanceState);
        }
        //initiateWindow();
        //addSearchListener();
    }

    private void initiateServices(){
        categoryService = ViewModelProviders.of(this, Factory.getCategoryServiceFactory()).get(CategoryService.class);
        productService = ViewModelProviders.of(this, Factory.getProductServiceFactory()).get(ProductService.class);
        searchService = ViewModelProviders.of(this, Factory.getSearchServiceFactory()).get(SearchService.class);
    }

    private void extractBundle(Bundle savedInstanceState){
        are_products_displayed = savedInstanceState.getBoolean(getResources().getString(R.string.received_search));
        whichProductsDisplayed = savedInstanceState.getInt(getResources().getString(R.string.displayed));
        currentCategory = (Category) savedInstanceState.getSerializable(getResources().getString(R.string.category));
        categoryCluster = (CategoryCluster) savedInstanceState.getSerializable(getResources().getString(R.string.cluster));
        metaCategoryChosen = (Category) savedInstanceState.getSerializable(getResources().getString(R.string.meta_category));
        searchText = savedInstanceState.getString(getResources().getString(R.string.search));
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(getResources().getString(R.string.received_search), are_products_displayed);
        outState.putInt(getResources().getString(R.string.displayed), whichProductsDisplayed);
        outState.putSerializable(getResources().getString(R.string.category), currentCategory);
        outState.putSerializable(getResources().getString(R.string.cluster), categoryCluster);
        outState.putSerializable(getResources().getString(R.string.meta_category), metaCategoryChosen);
        outState.putString(getResources().getString(R.string.search), searchText);
    }
}
