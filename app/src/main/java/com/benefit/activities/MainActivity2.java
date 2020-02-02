package com.benefit.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.benefit.R;
import com.benefit.activities.ui.mainactivity2.MainActivity2Fragment;
import com.benefit.model.User;

public class MainActivity2 extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        if (savedInstanceState == null) {

        }
    }
}
