package com.benefit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemsPageActivity extends AppCompatActivity {

    private static final int CATEGORIES_DISPLAYED = 1;
    private static final int CLUSTERS_DISPLAYED = 2;

    private List<PropertyName> allFilters;
    private Category currentCategory;
    private Category metaCategoryChosen;
    private CategoryCluster categoryCluster;
    private Map<String, List<String>> currentFilters;
    private AllProductsScreen activityScreen;
    private boolean refresh = false;
    private int whichProductsDisplayed;
    private DatabaseDriver databaseDriver = new DatabaseDriver();
    private CategoryService categoryService;
    private ProductService productService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentFilters = new HashMap<>();
        allFilters = new ArrayList<>();
        setHeaderListeners();
        Bundle bundle = getIntent().getExtras();
        extractExtras(bundle);
        initiateWindow();
    }

    private void extractExtras(Bundle bundle) {
        switch (bundle.getInt("displayed")) {
            case CLUSTERS_DISPLAYED:
                whichProductsDisplayed = CLUSTERS_DISPLAYED;
                currentCategory = null;
                categoryCluster = (CategoryCluster) bundle.getSerializable("cluster");
                metaCategoryChosen = null;
                break;
            case CATEGORIES_DISPLAYED:
                whichProductsDisplayed = CATEGORIES_DISPLAYED;
                currentCategory = (Category) bundle.getSerializable("category");
                if (bundle.getString("metaExists").equals("true")) {
                    metaCategoryChosen = (Category) bundle.get("metaCategory");
                } else {
                    metaCategoryChosen = null;
                }
                categoryCluster = null;
                break;
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

    public void openFilter(View view) {
        activityScreen.openFilter(view, allFilters, currentFilters);
        setPopupOnClickListeners(activityScreen.getPopupView(), activityScreen.getPopup(), activityScreen.getFilterPopup());
    }

    private void setPopupOnClickListeners(View layout, final PopupWindow popup, final FilterPopup filterPopup) {
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
                refresh = true;
                getAllFilteredItems();
            }
        });
    }

    private void getAllFilteredItems() {
        switch (whichProductsDisplayed) {
            case CLUSTERS_DISPLAYED:
                for (int id : categoryCluster.getCategoryIdList()) {
                    showFilteredProducts(id);
                }
                break;
            case CATEGORIES_DISPLAYED:
                showFilteredProducts(currentCategory.getId());
                break;
        }
    }

    private void showFilteredProducts(int categoryId) {
        final Observer<List<Product>> productObserver = new Observer<List<Product>>() {

            @Override
            public void onChanged(List<Product> products) {
                if (refresh) {
                    activityScreen.refreshTable(products);
                    refresh = false;
                } else {
                    activityScreen.addDisplayTable(products);
                }
                addProductListeners();
            }
        };
        productService.getProductsByProperties(categoryId, currentFilters).observe(this, productObserver);

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

    private void initiateWindow() {
        addScreenSettings();
        activityScreen = new AllProductsScreen(findViewById(android.R.id.content).getRootView(), currentCategory, categoryCluster);
        createServices();
        getAllMetaCategories();
        addProductsToScreen();
        if (whichProductsDisplayed == CATEGORIES_DISPLAYED) {
            getFilters(currentCategory.getId());
        } else {
            getAllCategoryFilters();
        }
        activityScreen.writeFiltersOnScreen(currentFilters);
    }

    private void addScreenSettings() {
        setContentView(R.layout.activity_items_page);
        findViewById(R.id.search_icon).setBackground(getResources().getDrawable(R.drawable.ic_search_icon_color));
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void createServices() {
        createCategoryService();
        createProductService();
    }

    private void getAllCategoryFilters() {
        for (int categoryId : categoryCluster.getCategoryIdList()) {
            getFilters(categoryId);
        }
    }

    private void addProductsToScreen() {
        switch (whichProductsDisplayed) {
            case CLUSTERS_DISPLAYED:
                for (int categoryId : categoryCluster.getCategoryIdList()) {
                    addProducts(categoryId);
                }
                break;
            case CATEGORIES_DISPLAYED:
                addProducts(currentCategory.getId());
                break;
        }
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

    private void getFilters(int categoryId) {
        final Observer<List<PropertyName>> propertiesObserver = new Observer<List<PropertyName>>() {

            @Override
            public void onChanged(List<PropertyName> properties) {
                allFilters.addAll(properties);
            }
        };
        categoryService.getAllPropertiesByCategoryId(categoryId).observe(this, propertiesObserver);
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

    private void addProducts(int categoryId) {
        final Observer<List<Product>> productObserver = new Observer<List<Product>>() {

            @Override
            public void onChanged(List<Product> products) {
                activityScreen.addDisplayTable(products);
                addProductListeners();
            }
        };
        productService.getProductsByCategoryId(categoryId).observe(this, productObserver);

    }

    private void setHeaderListeners() {
        findViewById(R.id.give_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGiveActivity();
            }
        });

        findViewById(R.id.give_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUserProfileActivity();
            }
        });

        findViewById(R.id.give_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearchActivity();
            }
        });

        findViewById(R.id.give_icon).setOnClickListener(new View.OnClickListener() {
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
