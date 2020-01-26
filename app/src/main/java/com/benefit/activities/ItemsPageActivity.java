package com.benefit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.benefit.R;
import com.benefit.adapters.DisplayableRecycleAdapter;
import com.benefit.drivers.DatabaseDriver;
import com.benefit.model.Category;
import com.benefit.model.CategoryCluster;
import com.benefit.model.Product;
import com.benefit.model.PropertyName;
import com.benefit.services.CategoryService;
import com.benefit.services.ProductService;
import com.benefit.services.SearchService;
import com.benefit.ui.Displayable;
import com.benefit.ui.items.FilterPopup;
import com.benefit.ui.items.ItemsPageUI;
import com.benefit.utilities.HeaderClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The activity the screen where the items are shown
 */
public class ItemsPageActivity extends AppCompatActivity {

    private static final int ONE_CATEGORY_DISPLAYED = 1;
    private static final int MULTIPLE_CATEGORIES_DISPLAYED = 2;
    private static final int CATEGORY_PRODUCTS = 1;
    private static final int FILTERED_PRODUCTS = 2;
    private static final int ALL_CATEGORY_SEARCH = 1;
    private static final int SPECIFIC_CATEGORY_SEARCH = 2;

    private boolean are_products_displayed = false;
    private int whichProductsDisplayed;
    private Category currentCategory;
    private Category metaCategoryChosen;
    private CategoryCluster categoryCluster;
    private List<PropertyName> allFilters;
    private Map<String, List<String>> currentFilters;
    private ItemsPageUI pageUI;
    private DatabaseDriver databaseDriver = new DatabaseDriver();
    private CategoryService categoryService;
    private ProductService productService;
    private SearchService searchService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentFilters = new HashMap<>();
        allFilters = new ArrayList<>();
        createServices();

        extractExtras();
        initiateWindow();
        addSearchListener();
        setHeaderListeners();
    }

    private void extractExtras() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getBoolean("searchReceived")) {
                search(ALL_CATEGORY_SEARCH, bundle.getString("searchResult"));
                are_products_displayed = true;
            } else {
                extractProductsFromCategories(bundle);

            }
        }
    }

    private void extractProductsFromCategories(Bundle bundle) {
        switch (bundle.getInt("displayed")) {
            case MULTIPLE_CATEGORIES_DISPLAYED:
                whichProductsDisplayed = MULTIPLE_CATEGORIES_DISPLAYED;
                currentCategory = null;
                categoryCluster = (CategoryCluster) bundle.getSerializable("cluster");
                metaCategoryChosen = null;
                break;
            case ONE_CATEGORY_DISPLAYED:
                whichProductsDisplayed = ONE_CATEGORY_DISPLAYED;
                currentCategory = (Category) bundle.getSerializable("category");
                if (bundle.getBoolean("metaExists")) {
                    metaCategoryChosen = (Category) bundle.get("metaCategory");
                } else {
                    metaCategoryChosen = null;
                }
                categoryCluster = null;
                break;
        }
    }

    private void search(int whatSearched, String searchText) {
        final Observer<List<Product>> searchObserver = new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                pageUI.addDisplayTable(products);
                addProductListeners();
            }
        };
        searchInCategory(whatSearched, searchText, searchObserver);
    }

    private void searchInCategory(int whatSearched, String searchText, Observer<List<Product>> searchObserver) {
        switch (whatSearched) {
            case ALL_CATEGORY_SEARCH:
                searchService.getProductsBySearchString(searchText).observe(this, searchObserver);
                break;
            case SPECIFIC_CATEGORY_SEARCH:
                switch (whichProductsDisplayed) {
                    case MULTIPLE_CATEGORIES_DISPLAYED:
                        for (int categoryId : categoryCluster.getCategoryIdList()) {
                            searchService.getProductsBySearchString(searchText, categoryId).
                                    observe(this, searchObserver);
                        }
                        break;
                    case ONE_CATEGORY_DISPLAYED:
                        searchService.getProductsBySearchString(searchText, (int) currentCategory.getId()).
                                observe(this, searchObserver);
                        break;
                }
        }
    }

    private void createServices() {
        createCategoryService();
        createProductService();
        createSearchService();
    }

    private void createSearchService() {
        ViewModelProvider.Factory searchServiceFactory = new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new SearchService(databaseDriver);
            }
        };
        this.searchService = ViewModelProviders.of(this, searchServiceFactory).get(SearchService.class);
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

    private void initiateWindow() {
        addScreenSettings();
        pageUI = new ItemsPageUI(findViewById(android.R.id.content).getRootView(), currentCategory, categoryCluster);
        getAllMetaCategories();
        if (!are_products_displayed) {
            addProductsToScreen();
            if (whichProductsDisplayed == ONE_CATEGORY_DISPLAYED) {
                getFilters((int) currentCategory.getId());
            } else {
                getAllCategoryFilters();
            }
            pageUI.openFilterPopup(currentFilters);
        }
    }

    private void addScreenSettings() {
        setContentView(R.layout.activity_items_page);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }

    private void getAllMetaCategories() {
        final Observer<List<Category>> metaCategoriesObserver = new Observer<List<Category>>() {

            @Override
            public void onChanged(List<Category> categories) {
                if (metaCategoryChosen == null) {
                    pageUI.addMetaCategoryBar(categories);
                } else {
                    pageUI.addMetaCategoryBar(categories, metaCategoryChosen);
                }
                addMetaCategoryListeners();
            }
        };
        categoryService.getAllMetaCategories().observe(this, metaCategoriesObserver);

    }

    private void addMetaCategoryListeners() {
        for (final Map.Entry<Category, Button> metaCategory : pageUI.getMetaCategoryButtonMap().entrySet()) {
            metaCategory.getValue().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (metaCategoryChosen != null && metaCategoryChosen.getName().equals(metaCategory.getKey().getName())) {
                        returnToHomePage();
                    } else {
                        returnToHomePage(metaCategory.getKey());
                    }
                }
            });
        }
    }

    private void addProductsToScreen() {
        switch (whichProductsDisplayed) {
            case MULTIPLE_CATEGORIES_DISPLAYED:
                for (int categoryId : categoryCluster.getCategoryIdList()) {
                    showProducts(CATEGORY_PRODUCTS, categoryId);
                }
                break;
            case ONE_CATEGORY_DISPLAYED:
                showProducts(CATEGORY_PRODUCTS, (int) currentCategory.getId());
                break;
        }
    }

    private void showProducts(int productsToShow, int categoryId) {
        final Observer<List<Product>> productObserver = new Observer<List<Product>>() {

            @Override
            public void onChanged(List<Product> products) {
                pageUI.addDisplayTable(products);
                addProductListeners();
            }
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
        pageUI.getDisplayableAdapter().setOnItemClickListener(new DisplayableRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Displayable itemClicked = pageUI.getDisplayableItems().get(position);
                displayProductInNewWindow((Product) itemClicked);
            }
        });
    }

    private void displayProductInNewWindow(Product productClicked) {
        Intent intent = new Intent(this, ProductPageActivity.class);
        intent.putExtra("product", productClicked);
        startActivity(intent);
    }

    private void getFilters(int categoryId) {
        final Observer<List<PropertyName>> propertiesObserver = new Observer<List<PropertyName>>() {

            @Override
            public void onChanged(List<PropertyName> properties) {
                for (PropertyName propertyName : properties) {
                    if (propertyName.getValidValues() != null &&
                            propertyNameNotInAllFilters(propertyName)) {
                        allFilters.add(propertyName);
                    }
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

    private void getAllCategoryFilters() {
        for (int categoryId : categoryCluster.getCategoryIdList()) {
            getFilters(categoryId);
        }
    }

    public void openFilterPopup(View view) {
        pageUI.openFilter(view, allFilters, currentFilters);
        setFilterOnClickListeners(pageUI.getPopupView(), pageUI.getPopup(), pageUI.getFilterPopup());
    }

    private void setFilterOnClickListeners(View layout, final PopupWindow popup, final FilterPopup filterPopup) {
        Button reset = layout.findViewById(R.id.reset_button);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterPopup.refreshFilter();
            }
        });

        Button done = layout.findViewById(R.id.done_button);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFilters = filterPopup.getCurrentFilters();
                popup.dismiss();
                pageUI.undimBackground();
                pageUI.openFilterPopup(currentFilters);
                pageUI.refreshTable();
                if (currentFilters.size() == 0) {
                    addProductsToScreen();
                } else {
                    showAllFilteredItems();
                }
            }
        });
    }

    private void showAllFilteredItems() {
        switch (whichProductsDisplayed) {
            case MULTIPLE_CATEGORIES_DISPLAYED:
                for (int id : categoryCluster.getCategoryIdList()) {
                    showProducts(FILTERED_PRODUCTS, id);
                }
                break;
            case ONE_CATEGORY_DISPLAYED:
                showProducts(FILTERED_PRODUCTS, (int) currentCategory.getId());
                break;
        }
    }

    private void addSearchListener() {
        SearchView searchView = findViewById(R.id.search_input);
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

    private void setHeaderListeners() {
        HeaderClickListener.setHeaderListeners(findViewById(android.R.id.content).getRootView());
    }

    private void returnToHomePage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void returnToHomePage(Category key) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("metaCategory", key);
        startActivity(intent);
    }
}
