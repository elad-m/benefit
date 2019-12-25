package com.benefit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ClothingRecyclerAdapter  extends RecyclerView.Adapter<ClothingRecyclerAdapter.ClothingViewHolder> {

    private ArrayList<ClothingItem> mClothingItems;
    public static class ClothingViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mEdit;
        public TextView mChat;

        public ClothingViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.item_image);
            mEdit = itemView.findViewById(R.id.edit_button);
            mChat = itemView.findViewById(R.id.chat_button);
        }
    }

    public ClothingRecyclerAdapter(ArrayList<ClothingItem> itemList){
        mClothingItems = itemList;
    }

    @NonNull
    @Override
    public ClothingRecyclerAdapter.ClothingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.clothing_item, parent, false);
        ClothingViewHolder evh = new ClothingViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ClothingRecyclerAdapter.ClothingViewHolder holder, int position) {
        ClothingItem currentItem = mClothingItems.get(position);

        holder.mImageView.setImageResource(currentItem.getmImageResource());
        holder.mEdit.setText(currentItem.getmEdit());
        holder.mChat.setText(currentItem.getmChat());

    }

    @Override
    public int getItemCount() {
        return mClothingItems.size();
    }
}
