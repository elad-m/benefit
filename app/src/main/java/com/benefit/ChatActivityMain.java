package com.benefit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.benefit.drivers.DatabaseDriver;
import com.benefit.viewmodel.ChatActivityViewModel;
import com.benefit.viewmodel.SignInViewModel;

public class ChatActivityMain extends AppCompatActivity {

    private DatabaseDriver databaseDriver;
    private ChatActivityViewModel cViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        databaseDriver = new DatabaseDriver();

        // View model
        cViewModel = ViewModelProviders.of(this).get(ChatActivityViewModel.class);

    }
}
