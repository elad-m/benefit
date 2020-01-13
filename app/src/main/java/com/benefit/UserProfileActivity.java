package com.benefit;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Present to the user its items (i.e. products). User can add, remove, open chat and edit its items.
 */
public class UserProfileActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ClothingRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<ClothingItem> mClothingItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        findViewById(R.id.search_icon).setBackground(getResources().getDrawable(R.drawable.ic_search_icon_color));
        
        // recycler elements
        mClothingItems.add(new ClothingItem(R.drawable.my_shoes, "Shoes"));
        mClothingItems.add(new ClothingItem(R.drawable.my_pants, "Pants"));
        mClothingItems.add(new ClothingItem(R.drawable.my_tshirt, "T-Shirt"));

        buildRecyclerView();
    }

    private void buildRecyclerView() {
        // recycler itself
        mRecyclerView = findViewById(R.id.items_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, 2);
        mAdapter = new ClothingRecyclerAdapter(mClothingItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ClothingRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String message = mClothingItems.get(position).getmTitle() + "was pressed";
                makeToast(message);
            }
        });
    }

    private void makeToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void onClickEdit(View view) {
        Toast.makeText(this, "Edit button was pressed", Toast.LENGTH_SHORT).show();
    }

    public void onClickChat(View view) {
        Toast.makeText(this, "Chat button was pressed", Toast.LENGTH_SHORT).show();
    }
}
