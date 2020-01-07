package com.benefit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ClothingRecyclerAdapter  extends RecyclerView.Adapter<ClothingRecyclerAdapter.ClothingViewHolder> {
    private ArrayList<ClothingItem> mClothingItems;

    static class ClothingViewHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        TextView mImageTitle;

        ClothingViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.item_image);
            mImageTitle = itemView.findViewById(R.id.item_title);
        }
    }

    ClothingRecyclerAdapter(ArrayList<ClothingItem> itemList){
        mClothingItems = itemList;
    }

    @NonNull
    @Override
    public ClothingRecyclerAdapter.ClothingViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.clothing_item, parent, false);
        v.findViewById(R.id.image_and_text_layout).setClipToOutline(true);
        ClothingViewHolder evh = new ClothingViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ClothingRecyclerAdapter.ClothingViewHolder holder, int position) {
        ClothingItem clothingItem = mClothingItems.get(position);
        // no "if empty" because clothingItem always has the resource data member initialized
        Picasso.get()
                .load(clothingItem.getmImageResource())
                .error(R.drawable.ic_image_placeholder)
                .placeholder(R.drawable.ic_image_placeholder)
                .centerCrop()
                .fit()
                .into(holder.mImageView);
        holder.mImageTitle.setText(clothingItem.getmTitle());
    }

    @Override
    public int getItemCount() {
        return mClothingItems.size();
    }
}
