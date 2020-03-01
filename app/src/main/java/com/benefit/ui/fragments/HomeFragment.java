package com.benefit.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.benefit.R;
import com.benefit.activities.MainActivity2;
import com.benefit.model.Category;
import com.benefit.model.CategoryCluster;
import com.benefit.services.CategoryService;
import com.benefit.ui.Displayable;
import com.benefit.ui.products.MetaCategoryBar;
import com.benefit.ui.products.ProductsDisplay;
import com.benefit.utilities.Factory;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This fragment is for the page that shows the categories
 */
public class HomeFragment extends Fragment {

    private static final int CATEGORIES = 1;
    private static final int CATEGORIES_DISPLAYED = 1;
    private static final int CLUSTERS_DISPLAYED = 2;

    public static HomeFragment getInstance(Category metaCategoryChosen){
        HomeFragment fragment = new HomeFragment();
        fragment.metaCategoryChosen = metaCategoryChosen;
        return fragment;
    }

    private ProductsDisplay productsDisplay;
    private MetaCategoryBar metaCategoryBar;
    private Category metaCategoryChosen;
    private CategoryService categoryService;
    private Button metaButtonChosen;
    private int itemsDisplayed;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null){
            metaCategoryChosen = (Category) savedInstanceState.getSerializable(getActivity().getString(R.string.meta_category));
        }
        initiateWindow();
        addSearchListener();
    }

    private void addCategoryListeners() {
        productsDisplay.getDisplayableRecycleAdapter().setOnItemClickListener(position -> {
            if (position < productsDisplay.getDisplayableProducts().size()) {
                Displayable productClicked = productsDisplay.getDisplayableProducts().get(position);
                if (itemsDisplayed == CLUSTERS_DISPLAYED || ((Category) productClicked).getIsLeaf()) {
                    ((MainActivity2) getActivity()).startAllProductsFragmentFromCategories(itemsDisplayed, productClicked, metaCategoryChosen);
                } else {
                    productsDisplay.refreshDisplay();
                    showChildrenOfParent(((Category) productClicked).getIdAsInt());
                }
            }
        });
    }

    private void addMetaCategoryListeners() {
        for (final Map.Entry<Category, Button> metaCategory : metaCategoryBar.getMetaCategoryButtonMap().entrySet()) {
            metaCategory.getValue().setOnClickListener(v -> {
                if (metaCategoryChosen == null) {
                    instantiateAndColor(metaCategory);
                    showChildrenOfParent(metaCategoryChosen.getIdAsInt());
                    itemsDisplayed = CATEGORIES_DISPLAYED;
                } else {
                    if (!metaCategoryChosen.getName().equals(metaCategory.getKey().getName())) {
                        metaButtonChosen.setBackground(getResources().getDrawable(R.drawable.oval));
                        metaButtonChosen.setTextColor(Color.BLACK);
                        instantiateAndColor(metaCategory);
                        showChildrenOfParent(metaCategoryChosen.getIdAsInt());
                        itemsDisplayed = CATEGORIES_DISPLAYED;
                    } else {
                        metaButtonChosen = metaCategory.getValue();
                        metaButtonChosen.setBackground(getResources().getDrawable(R.drawable.oval));
                        metaButtonChosen.setTextColor(Color.BLACK);
                        metaCategoryChosen = null;
                        showCategoryClusters();
                        itemsDisplayed = CLUSTERS_DISPLAYED;
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

    private void showChildrenOfParent(int parentId) {
        productsDisplay.refreshDisplay();
        final Observer<List<Category>> childCategoryObserver = categories -> {
            productsDisplay.populateDisplayTable(categories);
            addCategoryListeners();
        };
        categoryService.getChildrenByParentId(parentId).observe(this, childCategoryObserver);
    }

    private void addSearchListener() {
        SearchView searchView = getActivity().findViewById(R.id.search_input);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() >= 1) {
                    ((MainActivity2) getActivity()).startAllProductsFragmentFromSearch(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void initiateWindow(){
        setWindow();
        metaCategoryBar = new MetaCategoryBar(getView());
        productsDisplay = new ProductsDisplay(getView(), CATEGORIES);
        categoryService = ViewModelProviders.of(this, Factory.getCategoryServiceFactory()).get(CategoryService.class);
        showMetaCategories();
        showItemsOnScreen();
    }

    private void setWindow(){
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }

    private void showItemsOnScreen() {
        if (metaCategoryChosen != null) {
            showChildrenOfParent(metaCategoryChosen.getIdAsInt());
            itemsDisplayed = CATEGORIES_DISPLAYED;
        } else {
            showCategoryClusters();
            itemsDisplayed = CLUSTERS_DISPLAYED;
        }
    }

    private void showMetaCategories() {
        final List<Category> metaCategories = new LinkedList<>();
        final Observer<List<Category>> metaCategoriesObserver = categories -> {
            metaCategories.addAll(categories);
            if (metaCategoryChosen == null) {
                metaCategoryBar.createCategoryBar(categories);
            } else {
                metaButtonChosen = metaCategoryBar.createCategoryBar(categories, metaCategoryChosen);
            }
            addMetaCategoryListeners();
        };
        categoryService.getAllMetaCategories().observe(this, metaCategoriesObserver);
    }

    private void showCategoryClusters() {
        productsDisplay.refreshDisplay();
        final Observer<List<CategoryCluster>> categoryObserver = clusters -> {
            productsDisplay.populateDisplayTable(clusters);
            addCategoryListeners();
        };
        categoryService.getAllHomepageCategoryClusters().observe(this, categoryObserver);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (metaCategoryChosen != null){
            outState.putSerializable(getActivity().getString(R.string.meta_category), metaCategoryChosen);
        }
    }
}
