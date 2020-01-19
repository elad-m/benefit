package com.benefit;

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

import com.benefit.UI.Displayable;
import com.benefit.UI.DisplayableRecycleAdapter;
import com.benefit.UI.Items.AllProductsScreen;
import com.benefit.UI.Items.FilterPopup;
import com.benefit.drivers.DatabaseDriver;
import com.benefit.model.Category;
import com.benefit.model.CategoryCluster;
import com.benefit.model.Product;
import com.benefit.model.PropertyName;
import com.benefit.services.CategoryService;
import com.benefit.services.ProductService;
import com.benefit.services.SearchService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemsPageActivity extends AppCompatActivity {

    private static final int ONE_CATEGORY_DISPLAYED = 1;
    private static final int MULTIPLE_CATEGORIES_DISPLAYED = 2;
    private static final int CATEGORY_PRODUCTS = 1;
    private static final int FILTERED_PRODUCTS = 2;
    private static final int ALL_CATEGORY_SEARCH = 1;
    private static final int SPECIFIC_CATEGORY_SEARCH = 2;

    private boolean are_products_displayed = false;
    private List<PropertyName> allFilters;
    private Category currentCategory;
    private Category metaCategoryChosen;
    private CategoryCluster categoryCluster;
    private Map<String, List<String>> currentFilters;
    private AllProductsScreen activityScreen;
    private int whichProductsDisplayed;
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
        Bundle bundle = getIntent().getExtras();
        extractExtras(bundle);
        initiateWindow();
        addSearchListener();
        setHeaderListeners();
    }

    private void extractExtras(Bundle bundle) {
        if (bundle.getBoolean("searchReceived")) {
            search(ALL_CATEGORY_SEARCH, bundle.getString("searchResult"));
            are_products_displayed = true;
        } else {
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
    }

    private void search(int whatSearched, String searchResult) {
        final Observer<List<Product>> searchObserver = new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                activityScreen.addDisplayTable(products);
                addProductListeners();
            }
        };
        switch (whatSearched) {
            case ALL_CATEGORY_SEARCH:
                searchService.getProductsBySearchString(searchResult).observe(this, searchObserver);
                break;
            case SPECIFIC_CATEGORY_SEARCH:
                switch (whichProductsDisplayed) {
                    case MULTIPLE_CATEGORIES_DISPLAYED:
                        for (int categoryId : categoryCluster.getCategoryIdList()) {
                            searchService.getProductsBySearchString(searchResult, categoryId).
                                    observe(this, searchObserver);
                        }
                        break;
                    case ONE_CATEGORY_DISPLAYED:
                        searchService.getProductsBySearchString(searchResult, currentCategory.getId()).
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
        activityScreen = new AllProductsScreen(findViewById(android.R.id.content).getRootView(), currentCategory, categoryCluster);
        getAllMetaCategories();
        if (!are_products_displayed) {
            addProductsToScreen();
            if (whichProductsDisplayed == ONE_CATEGORY_DISPLAYED) {
                getFilters(currentCategory.getId());
            } else {
                getAllCategoryFilters();
            }
            activityScreen.writeFiltersOnScreen(currentFilters);
        }
    }

    private void addScreenSettings() {
        setContentView(R.layout.activity_items_page);
        findViewById(R.id.search_icon).setBackground(getResources().getDrawable(R.drawable.ic_search_icon_color));
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void getAllMetaCategories() {
        final Observer<List<Category>> metaCategoriesObserver = new Observer<List<Category>>() {

            @Override
            public void onChanged(List<Category> categories) {
                activityScreen.addMetaCategoryBar(categories, metaCategoryChosen);
                addMetaCategoryListeners();
            }
        };
        categoryService.getAllMetaCategories().observe(this, metaCategoriesObserver);

    }

    private void addMetaCategoryListeners() {
        for (final Map.Entry<Category, Button> metaCategory : activityScreen.getMetaCategoryButtonMap().entrySet()) {
            metaCategory.getValue().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (metaCategoryChosen != null && metaCategoryChosen.getName().equals(metaCategory.getKey().getName())) {
                        startMainActivity();
                    } else {
                        startMainActivity(metaCategory.getKey());
                    }
                }
            });
        }
    }

    private void startMainActivity(Category key) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("metaCategory", key);
        startActivity(intent);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void addProductsToScreen() {
        switch (whichProductsDisplayed) {
            case MULTIPLE_CATEGORIES_DISPLAYED:
                for (int categoryId : categoryCluster.getCategoryIdList()) {
                    showProducts(CATEGORY_PRODUCTS, categoryId);
                }
                break;
            case ONE_CATEGORY_DISPLAYED:
                showProducts(CATEGORY_PRODUCTS, currentCategory.getId());
                break;
        }
    }

    private void showProducts(int productsToShow, int categoryId) {
        final Observer<List<Product>> productObserver = new Observer<List<Product>>() {

            @Override
            public void onChanged(List<Product> products) {
                activityScreen.addDisplayTable(products);
                addProductListeners();
            }
        };
        switch (productsToShow) {
            case CATEGORY_PRODUCTS:
                productService.getProductsByCategoryId(categoryId).observe(this, productObserver);
            case FILTERED_PRODUCTS:
                productService.getProductsByProperties(categoryId, currentFilters).observe(this, productObserver);

        }
    }

    private void addProductListeners() {
        activityScreen.getmAdapter().setOnItemClickListener(new DisplayableRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Displayable itemClicked = activityScreen.getDisplayableItems().get(position);
                displayProductInNewWindow((Product) itemClicked);
            }
        });
    }

    private void displayProductInNewWindow(Product productClicked) {
        //todo undelete this when the page is added
//        Intent intent = new Intent(this, ProductPageActivity.class);
//        intent.putExtra("product", productClicked);
//        startActivity(intent);
    }

    private void getFilters(int categoryId) {
        final Observer<List<PropertyName>> propertiesObserver = new Observer<List<PropertyName>>() {

            @Override
            public void onChanged(List<PropertyName> properties) {
                allFilters.addAll(properties);
            }
        };
        categoryService.getAllPropertiesByCategoryId(categoryId).observe(this, propertiesObserver);
    }

    private void getAllCategoryFilters() {
        for (int categoryId : categoryCluster.getCategoryIdList()) {
            getFilters(categoryId);
        }
    }

    public void openFilter(View view) {
        activityScreen.openFilter(view, allFilters, currentFilters);
        setFilterOnClickListeners(activityScreen.getPopupView(), activityScreen.getPopup(), activityScreen.getFilterPopup());
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
                activityScreen.writeFiltersOnScreen(currentFilters);
                activityScreen.refreshTable();
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
                showProducts(FILTERED_PRODUCTS, currentCategory.getId());
                break;
        }
    }

    private void addSearchListener() {
        SearchView searchView = findViewById(R.id.search_input);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() >= 1) {
                    search(SPECIFIC_CATEGORY_SEARCH, newText);
                }
                return false;
            }
        });
    }

    private void setHeaderListeners() {
        findViewById(R.id.give_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGiveActivity();
            }
        });

        findViewById(R.id.user_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUserProfileActivity();
            }
        });

        findViewById(R.id.search_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearchActivity();
            }
        });

        findViewById(R.id.message_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMessageActivity();
            }
        });
    }

    private void startMessageActivity() {
        Intent intent = new Intent(this, ConversationActivity.class);
        startActivity(intent);
    }

    private void startSearchActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void startUserProfileActivity() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

    private void startGiveActivity() {
        Intent intent = new Intent(this, GiveItemActivity.class);
        startActivity(intent);
    }

}
