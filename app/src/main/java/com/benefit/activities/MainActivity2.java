package com.benefit.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.benefit.R;
import com.benefit.model.User;

public class MainActivity2 extends AppCompatActivity {

    private View chosenView;
    private FrameLayout settingButton;
    private Button searchButton, userProfileButton, giveButton;
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
    }

    public void startGiveFragment(View view) {
        chosenView.setVisibility(View.INVISIBLE);
        giveButton.setBackground(getDrawable(R.drawable.ic_give_colored));
        userProfileButton.setBackground(getDrawable(R.drawable.ic_user_icon));
        searchButton.setBackground(getDrawable(R.drawable.ic_search_icon));
    }
}
