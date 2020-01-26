package com.benefit.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.benefit.R;
import com.benefit.ui.Displayable;
import com.benefit.adapters.DisplayableRecycleAdapter;
import com.benefit.ui.items.ItemsDisplay;
import com.benefit.ui.items.MetaCategoryBar;
import com.benefit.drivers.DatabaseDriver;
import com.benefit.model.Category;
import com.benefit.model.CategoryCluster;
import com.benefit.services.CategoryService;
import com.benefit.utilities.HeaderClickListener;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The activity for the page that shows the categories
 */
public class MainActivity extends AppCompatActivity {

    private static final int CATEGORIES = 1;
    private static final int CATEGORIES_DISPLAYED = 1;
    private static final int CLUSTERS_DISPLAYED = 2;
    private ItemsDisplay itemsDisplay;
    private MetaCategoryBar metaCategoryBar;
    private Category metaCategoryChosen;
    private Button metaButtonChosen;
    private int itemsDisplayed;
    private DatabaseDriver databaseDriver = new DatabaseDriver();
    private CategoryService categoryService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extractExtras();
        initiateWindow();
        setHeaderListeners();
        addSearchListener();
    }

    private void extractExtras() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            metaCategoryChosen = null;
        } else {
            metaCategoryChosen = (Category) bundle.getSerializable("metaCategory");
        }
    }

    private void addCategoryListeners() {
        itemsDisplay.getDisplayableRecycleAdapter().setOnItemClickListener(new DisplayableRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position < itemsDisplay.getDisplayableItems().size()) {
                    Displayable itemClicked = itemsDisplay.getDisplayableItems().get(position);
                    if (itemsDisplayed == CLUSTERS_DISPLAYED || ((Category) itemClicked).getIsLeaf()) {
                        openProductsPage(itemClicked);
                    } else {
                        itemsDisplay.refreshDisplay();
                        showChildrenOfParent((int)((Category) itemClicked).getId());
                    }
                }
            }
        });
    }

    private void openProductsPage(Displayable displayable) {
        Intent intent = new Intent(this, ItemsPageActivity.class);
        switch (itemsDisplayed) {
            case CLUSTERS_DISPLAYED:
                intent.putExtra("displayed", CLUSTERS_DISPLAYED);
                intent.putExtra("cluster", (CategoryCluster) displayable);
                break;
            case CATEGORIES:
                intent.putExtra("displayed", CATEGORIES_DISPLAYED);
                intent.putExtra("category", (Category) displayable);
                if (metaCategoryChosen == null) {
                    intent.putExtra("metaExists", false);
                } else {
                    intent.putExtra("metaExists", true);
                    intent.putExtra("metaCategory", metaCategoryChosen);
                }
                break;
        }
        startActivity(intent);
    }

    private void addMetaCategoryListeners() {
        for (final Map.Entry<Category, Button> metaCategory : metaCategoryBar.getMetaCategoryButtonMap().entrySet()) {
            metaCategory.getValue().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (metaCategoryChosen == null) {
                        instantiateAndColor(metaCategory);
                        showChildrenOfParent((int)metaCategoryChosen.getId());
                        itemsDisplayed = CATEGORIES_DISPLAYED;
                    } else {
                        if (!metaCategoryChosen.getName().equals(metaCategory.getKey().getName())) {
                            metaButtonChosen.setBackground(getResources().getDrawable(R.drawable.oval));
                            metaButtonChosen.setTextColor(Color.BLACK);
                            instantiateAndColor(metaCategory);
                            showChildrenOfParent((int)metaCategoryChosen.getId());
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

    private void initiateWindow() {
        addScreenSettings();
        metaCategoryBar = new MetaCategoryBar(findViewById(android.R.id.content).getRootView());
        itemsDisplay = new ItemsDisplay(findViewById(android.R.id.content).getRootView(), CATEGORIES);
        createCategoryService();
        showMetaCategories();
        showItemsOnScreen();
    }

    private void addScreenSettings() {
        setContentView(R.layout.activity_main);
        findViewById(R.id.search_icon).setBackground(getResources().getDrawable(R.drawable.ic_search_icon_color));
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }

    private void showItemsOnScreen() {
        if (metaCategoryChosen != null) {
            showChildrenOfParent((int)metaCategoryChosen.getId());
            itemsDisplayed = CATEGORIES_DISPLAYED;
        } else {
            showCategoryClusters();
            itemsDisplayed = CLUSTERS_DISPLAYED;
        }
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

    private void showMetaCategories() {
        final List<Category> metaCategories = new LinkedList<>();
        final Observer<List<Category>> metaCategoriesObserver = new Observer<List<Category>>() {

            @Override
            public void onChanged(List<Category> categories) {
                metaCategories.addAll(categories);
                if (metaCategoryChosen == null){
                    metaCategoryBar.createCategoryBar(metaCategories);
                } else {
                    metaButtonChosen = metaCategoryBar.createCategoryBar(metaCategories, metaCategoryChosen);
                }
                addMetaCategoryListeners();
            }
        };
        categoryService.getAllMetaCategories().observe(this, metaCategoriesObserver);

    }

    private void showCategoryClusters() {
        itemsDisplay.refreshDisplay();
        final List<CategoryCluster> categoryClusters = new LinkedList<>();
        final Observer<List<CategoryCluster>> categoryObserver = new Observer<List<CategoryCluster>>() {

            @Override
            public void onChanged(List<CategoryCluster> clusters) {
                categoryClusters.addAll(clusters);
                itemsDisplay.populateDisplayTable(categoryClusters);
                addCategoryListeners();
            }
        };
        categoryService.getAllHomepageCategoryClusters().observe(this, categoryObserver);
    }

    private void showChildrenOfParent(int parentId) {
        itemsDisplay.refreshDisplay();
        final Observer<List<Category>> childCategoryObserver = new Observer<List<Category>>() {

            @Override
            public void onChanged(List<Category> categories) {
                itemsDisplay.populateDisplayTable(categories);
                addCategoryListeners();
            }
        };
        categoryService.getChildrenByParentId(parentId).observe(this, childCategoryObserver);

    }

    private void addSearchListener() {
        SearchView searchView = findViewById(R.id.search_input);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() >= 1) {
                    startItemActivityWithSearch(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void startItemActivityWithSearch(String newText) {
        Intent intent = new Intent(this, ItemsPageActivity.class);
        intent.putExtra("searchReceived", true);
        intent.putExtra("searchResult", newText);
        startActivity(intent);
    }

    private void setHeaderListeners() {
        HeaderClickListener.setHeaderListeners(findViewById(android.R.id.content).getRootView());
    }


}