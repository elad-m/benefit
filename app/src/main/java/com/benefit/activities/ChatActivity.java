package com.benefit.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.benefit.R;
import com.benefit.model.Match;
import com.benefit.model.Product;

public class ChatActivity extends AppCompatActivity {

    private Match match;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
    }
}
