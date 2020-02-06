package com.benefit.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.benefit.R;
import com.benefit.model.Category;
import com.benefit.model.CategoryCluster;

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.all_products_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
