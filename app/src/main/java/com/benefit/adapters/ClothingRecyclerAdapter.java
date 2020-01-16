package com.benefit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.benefit.R;
import com.benefit.ui.profile.ClothingItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Adapter for the user's items RecyclerView.
 */
public class ClothingRecyclerAdapter  extends RecyclerView.Adapter<ClothingRecyclerAdapter.ClothingViewHolder> {
    private ArrayList<ClothingItem> mClothingItems;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return mListener;
    }

    static class ClothingViewHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        TextView mImageTitle;

        ClothingViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            // itemView is the whole card
            super(itemView);
            mImageView = itemView.findViewById(R.id.item_image);
            mImageTitle = itemView.findViewById(R.id.item_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public ClothingRecyclerAdapter(ArrayList<ClothingItem> itemList){
        mClothingItems = itemList;
    }

    @NonNull
    @Override
    public ClothingRecyclerAdapter.ClothingViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                         int viewType) {
        View clothingItemAsView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.clothing_item, parent, false);
        clothingItemAsView.findViewById(R.id.image_and_text_layout).setClipToOutline(true);
        return new ClothingViewHolder(clothingItemAsView, mListener);
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
