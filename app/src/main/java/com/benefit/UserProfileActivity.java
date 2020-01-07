package com.benefit;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // recycler elements
        ArrayList<ClothingItem> clothingItems = new ArrayList<>();
        clothingItems.add(new ClothingItem(R.drawable.my_shoes, "Shoes"));
        clothingItems.add(new ClothingItem(R.drawable.my_shoes, "Shoes another"));
        clothingItems.add(new ClothingItem(R.drawable.my_shoes, "Shoes 3"));

        // recycler itself
        mRecyclerView = findViewById(R.id.items_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, 2);
        mAdapter = new ClothingRecyclerAdapter(clothingItems);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


    }

}
