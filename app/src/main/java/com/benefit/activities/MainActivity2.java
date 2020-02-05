package com.benefit.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.benefit.R;
import com.benefit.drivers.DatabaseDriver;
import com.benefit.drivers.StorageDriver;
import com.benefit.model.User;
import com.benefit.services.CategoryService;
import com.benefit.services.ProductService;
import com.benefit.services.SearchService;
import com.benefit.services.UserService;
import com.benefit.ui.MainFragment;
import com.benefit.utilities.Factory;

/**
 * This is the main activity.
 */
public class MainActivity2 extends AppCompatActivity {

    public DatabaseDriver databaseDriver = new DatabaseDriver();
    public StorageDriver storageDriver;
    public CategoryService categoryService;
    public ProductService productService;
    public SearchService searchService;
    public UserService userService;

    private View chosenView;
    private FrameLayout settingButton;
    private Button homeButton, searchButton, userProfileButton, giveButton;
    private Fragment activityFragment;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        if (savedInstanceState == null) {

        }
        user = (User) getIntent().getSerializableExtra(getString(R.string.user_relay));

        initiateServices();
        initiateViewElements();

    }

    private void initiateServices(){
        productService = ViewModelProviders.of(this,
                Factory.getProductServiceFactory()).get(ProductService.class);
        categoryService = ViewModelProviders.of(this,
                Factory.getCategoryServiceFactory()).get(CategoryService.class);
        searchService = ViewModelProviders.of(this,
                Factory.getSearchServiceFactory()).get(SearchService.class);
        userService = ViewModelProviders.of(this,
                Factory.getUserServiceFactory()).get(UserService.class);
        this.storageDriver = ViewModelProviders.of(this,
                Factory.getStorageDriverFactory()).get(StorageDriver.class);
    }

    private void initiateViewElements(){
        chosenView = findViewById(R.id.chosen_view);
        settingButton = findViewById(R.id.sandwich_icon);
        searchButton = findViewById(R.id.search_icon);
        userProfileButton = findViewById(R.id.user_icon);
        giveButton = findViewById(R.id.give_icon);
        activityFragment = getSupportFragmentManager().findFragmentById(R.id.activity_fragment);
    }

    public void startSearchFragment(View view) {
        ConstraintLayout constraintLayout = findViewById(R.id.header);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(R.id.chosen_view, ConstraintSet.RIGHT, R.id.search_icon, ConstraintSet.RIGHT);
        constraintSet.connect(R.id.chosen_view, ConstraintSet.LEFT, R.id.search_icon, ConstraintSet.LEFT);
        constraintSet.applyTo(constraintLayout);

        chosenView.setVisibility(View.VISIBLE);
        giveButton.setBackground(getDrawable(R.drawable.ic_give_icon));
        userProfileButton.setBackground(getDrawable(R.drawable.ic_user_icon));
        searchButton.setBackground(getDrawable(R.drawable.ic_search_icon_color));

        MainFragment fragment = new MainFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.activity_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void startUserProfileFragment(View view) {
        ConstraintLayout constraintLayout = findViewById(R.id.header);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(R.id.chosen_view, ConstraintSet.RIGHT, R.id.user_icon, ConstraintSet.RIGHT);
        constraintSet.connect(R.id.chosen_view, ConstraintSet.LEFT, R.id.user_icon, ConstraintSet.LEFT);
        constraintSet.applyTo(constraintLayout);

        chosenView.setVisibility(View.VISIBLE);
        giveButton.setBackground(getDrawable(R.drawable.ic_give_icon));
        userProfileButton.setBackground(getDrawable(R.drawable.ic_user_colored));
        searchButton.setBackground(getDrawable(R.drawable.ic_search_icon));

        /*
        UserProfileFragment fragment = new UserProfileFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.activity_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
         */
    }

    public void startGiveFragment(View view) {
        chosenView.setVisibility(View.INVISIBLE);
        giveButton.setBackground(getDrawable(R.drawable.ic_give_colored));
        userProfileButton.setBackground(getDrawable(R.drawable.ic_user_icon));
        searchButton.setBackground(getDrawable(R.drawable.ic_search_icon));

        /*
        GiveFragment fragment = new GiveFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.activity_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
         */
    }
}
