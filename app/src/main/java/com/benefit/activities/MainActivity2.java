package com.benefit.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.benefit.R;
import com.benefit.drivers.AuthenticationDriver;
import com.benefit.model.Category;
import com.benefit.model.Product;
import com.benefit.model.User;
import com.benefit.ui.AllProductsFragment;
import com.benefit.ui.Displayable;
import com.benefit.ui.GiveFragment;
import com.benefit.ui.HomeFragment;
import com.benefit.ui.ProductFragment;
import com.benefit.ui.ProfileFragment;
import com.google.android.material.navigation.NavigationView;

/**
 * This is the main activity.
 */
public class MainActivity2 extends AppCompatActivity {

    private AuthenticationDriver authenticationDriver = new AuthenticationDriver();
    private View chosenView;
    private FrameLayout settingButton;
    private Button homeButton, userProfileButton, giveButton;
    private Fragment activityFragment;
    private NavigationView menu;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        user = (User) getIntent().getSerializableExtra(getString(R.string.user_relay));

        initiateViewElements();

    }


    private void initiateViewElements(){
        chosenView = findViewById(R.id.chosen_view);
        settingButton = findViewById(R.id.sandwich_icon);
        homeButton = findViewById(R.id.home_icon);
        userProfileButton = findViewById(R.id.user_icon);
        giveButton = findViewById(R.id.give_icon);
        activityFragment = getSupportFragmentManager().findFragmentById(R.id.activity_fragment);
        menu = findViewById(R.id.setting_navigation);
        menu.setNavigationItemSelectedListener(item -> {
            if (item.getTitle().equals(getString(R.string.sign_out))){
                signOut();
            }
            return true;
        });
    }

    public void openSettingMenu(View view){
        ((DrawerLayout) findViewById(R.id.drawer_layout)).openDrawer(findViewById(R.id.setting_navigation));
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
        homeButton.setBackground(getDrawable(R.drawable.ic_home_colored));

        replaceFragment(HomeFragment.getInstance(metaCategoryChosen));
    }

    public void startAllProductsFragmentFromCategories(int itemsDisplayed, Displayable displayable, Category metaCategoryChosen){
        replaceFragment(AllProductsFragment.newInstance(false, null, itemsDisplayed, displayable, metaCategoryChosen));
    }

    public void startAllProductsFragmentFromSearch(String searchTest){
        replaceFragment(AllProductsFragment.newInstance(true, searchTest, 0, null, null));
    }

    public void startProductFragment(Product product){
        replaceFragment(ProductFragment.newInstance(product, user));
    }

    public void startProfileFragmentOnClick(View view) {
        startProfileFragment();
    }

    public void startProfileFragment(){
        ConstraintLayout constraintLayout = findViewById(R.id.header);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(R.id.chosen_view, ConstraintSet.RIGHT, R.id.user_icon, ConstraintSet.RIGHT);
        constraintSet.connect(R.id.chosen_view, ConstraintSet.LEFT, R.id.user_icon, ConstraintSet.LEFT);
        constraintSet.applyTo(constraintLayout);

        chosenView.setVisibility(View.VISIBLE);
        giveButton.setBackground(getDrawable(R.drawable.ic_give_icon));
        userProfileButton.setBackground(getDrawable(R.drawable.ic_user_colored));
        homeButton.setBackground(getDrawable(R.drawable.ic_home_icon));

        replaceFragment(ProfileFragment.newInstance(user));
    }

    public void startGiveFragmentOnClick(View view) {
        startGiveFragment(null);
    }

    public void startGiveFragment(Product product){
        chosenView.setVisibility(View.INVISIBLE);
        giveButton.setBackground(getDrawable(R.drawable.ic_give_colored));
        userProfileButton.setBackground(getDrawable(R.drawable.ic_user_icon));
        homeButton.setBackground(getDrawable(R.drawable.ic_home_icon));

        replaceFragment(GiveFragment.newInstance(user, product));
    }

    public void signOut(){
        authenticationDriver.getAuth().signOut();
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }
}
