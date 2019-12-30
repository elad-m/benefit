package com.benefit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.benefit.drivers.DatabaseDriver;

public class MainActivity extends AppCompatActivity {

    private static final boolean TESTING = true;

    private DatabaseDriver databaseDriver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseDriver = new DatabaseDriver();


        if(TESTING){

        }
    }

}
