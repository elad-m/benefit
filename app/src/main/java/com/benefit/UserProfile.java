package com.benefit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private CircleImageView profileImage;
    private static final int PICK_IMAGE = 1;
    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        ArrayList<ClothingItem> clothingItems = new ArrayList<>();
        clothingItems.add(new ClothingItem(R.drawable.ic_shirt, "edit", "chat"));
        clothingItems.add(new ClothingItem(R.drawable.ic_shirt, "edit", "chat"));
        clothingItems.add(new ClothingItem(R.drawable.ic_jacket, "edit", "chat"));
        clothingItems.add(new ClothingItem(R.drawable.ic_jacket, "edit", "chat"));
        clothingItems.add(new ClothingItem(R.drawable.ic_shirt, "edit", "chat"));
        clothingItems.add(new ClothingItem(R.drawable.ic_shirt, "edit", "chat"));
        clothingItems.add(new ClothingItem(R.drawable.ic_jacket, "edit", "chat"));
        clothingItems.add(new ClothingItem(R.drawable.ic_jacket, "edit", "chat"));

        mRecyclerView = findViewById(R.id.items_recyclerk);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this, 2);
        mAdapter = new ClothingRecyclerAdapter(clothingItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        profileImage = findViewById(R.id.profile_image);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            imageUri = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
