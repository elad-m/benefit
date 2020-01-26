package com.benefit.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.benefit.R;
import com.benefit.adapters.ClothingRecyclerAdapter;
import com.benefit.model.User;
import com.benefit.utilities.HeaderClickListener;
import com.benefit.ui.profile.ClothingItem;

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

        ConstraintLayout constraintLayout = findViewById(R.id.page_header_root_layout);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(R.id.chosen_view, ConstraintSet.RIGHT, R.id.user_icon, ConstraintSet.RIGHT);
        constraintSet.connect(R.id.chosen_view, ConstraintSet.LEFT, R.id.user_icon, ConstraintSet.LEFT);
        constraintSet.applyTo(constraintLayout);
        setHeaderListeners();

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

    private void setHeaderListeners() {
        HeaderClickListener.setHeaderListeners(findViewById(android.R.id.content).getRootView());
    }
}