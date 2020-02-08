package com.benefit.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.benefit.R;
import com.benefit.drivers.DatabaseDriver;
import com.benefit.drivers.StorageDriver;
import com.benefit.model.Category;
import com.benefit.model.Product;
import com.benefit.model.User;
import com.benefit.services.CategoryService;
import com.benefit.services.ProductService;
import com.benefit.services.SearchService;
import com.benefit.services.UserService;
import com.benefit.ui.AllProductsFragment;
import com.benefit.ui.Displayable;
import com.benefit.ui.HomeFragment;
import com.benefit.utilities.Factory;

/**
 * This is the main activity.
 */
public class MainActivity2 extends AppCompatActivity {

    private View chosenView;
    private FrameLayout settingButton;
    private Button homeButton, mainButton, userProfileButton, giveButton;
    private Fragment activityFragment;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        if (savedInstanceState == null) {

        }
        user = (User) getIntent().getSerializableExtra(getString(R.string.user_relay));

        initiateViewElements();

    }


    private void initiateViewElements(){
        chosenView = findViewById(R.id.chosen_view);
        settingButton = findViewById(R.id.sandwich_icon);
        mainButton = findViewById(R.id.home_icon);
        userProfileButton = findViewById(R.id.user_icon);
        giveButton = findViewById(R.id.give_icon);
        activityFragment = getSupportFragmentManager().findFragmentById(R.id.activity_fragment);
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.activity_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void startHomeFragmentOnClick(View view) {
        startHomeFragment(null);
    }

    public void startHomeFragment(Category metaCategoryChosen){
        ConstraintLayout constraintLayout = findViewById(R.id.header);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(R.id.chosen_view, ConstraintSet.RIGHT, R.id.home_icon, ConstraintSet.RIGHT);
        constraintSet.connect(R.id.chosen_view, ConstraintSet.LEFT, R.id.home_icon, ConstraintSet.LEFT);
        constraintSet.applyTo(constraintLayout);

        chosenView.setVisibility(View.VISIBLE);
        giveButton.setBackground(getDrawable(R.drawable.ic_give_icon));
        userProfileButton.setBackground(getDrawable(R.drawable.ic_user_icon));
        mainButton.setBackground(getDrawable(R.drawable.ic_home_colored));

        replaceFragment(HomeFragment.getInstance(metaCategoryChosen));
    }

    public void startAllProductsFragmentFromCategories(int itemsDisplayed, Displayable displayable, Category metaCategoryChosen){
        replaceFragment(AllProductsFragment.newInstance(false, null, itemsDisplayed, displayable, metaCategoryChosen));
    }

    public void startAllProductsFragmentFromSearch(String searchTest){
        replaceFragment(AllProductsFragment.newInstance(true, searchTest, 0, null, null));
    }

    public void startProductFragment(Product product){

    }

    public void startUserProfileFragmentOnClick(View view) {
        ConstraintLayout constraintLayout = findViewById(R.id.header);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(R.id.chosen_view, ConstraintSet.RIGHT, R.id.user_icon, ConstraintSet.RIGHT);
        constraintSet.connect(R.id.chosen_view, ConstraintSet.LEFT, R.id.user_icon, ConstraintSet.LEFT);
        constraintSet.applyTo(constraintLayout);

        chosenView.setVisibility(View.VISIBLE);
        giveButton.setBackground(getDrawable(R.drawable.ic_give_icon));
        userProfileButton.setBackground(getDrawable(R.drawable.ic_user_colored));
        mainButton.setBackground(getDrawable(R.drawable.ic_home_icon));

        /*
        replaceFragment(new UserProfileFragment());
         */
    }

    public void startGiveFragmentOnClick(View view) {
        chosenView.setVisibility(View.INVISIBLE);
        giveButton.setBackground(getDrawable(R.drawable.ic_give_colored));
        userProfileButton.setBackground(getDrawable(R.drawable.ic_user_icon));
        mainButton.setBackground(getDrawable(R.drawable.ic_home_icon));

        /*
        replaceFragment(new GiveFragment());
         */
    }
}
