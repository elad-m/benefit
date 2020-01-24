package com.benefit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
public class ClothingRecyclerAdapter extends RecyclerView.Adapter<ClothingRecyclerAdapter.ClothingViewHolder> {
    private ArrayList<ClothingItem> mClothingItems;
    private OnItemClickListener mListener;
    private RecyclerView mRecyclerView;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int posotion, View view);
        void onEditClick(int position, View view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return mListener;
    }

    static class ClothingViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView mImageTitle;
        Button mDeleteButton;
        Button mEditButton;

        ClothingViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            // itemView is the whole card
            super(itemView);
            mImageView = itemView.findViewById(R.id.item_image);
            mImageTitle = itemView.findViewById(R.id.item_title);
            mDeleteButton = itemView.findViewById(R.id.delete_button);
            mEditButton = itemView.findViewById(R.id.edit_button);

            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View deleteButtonView) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position, deleteButtonView);
                        }
                    }
                }
            });
            mEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View editButtonView) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onEditClick(position, editButtonView);
                        }
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public ClothingRecyclerAdapter(ArrayList<ClothingItem> itemList) {
        mClothingItems = itemList;
    }

    @NonNull
    @Override
    public ClothingRecyclerAdapter.ClothingViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                         int viewType) {
        View clothingItemAsView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.clothing_item, parent, false);
        clothingItemAsView.findViewById(R.id.image_layout).setClipToOutline(true);
        mRecyclerView = (RecyclerView) parent;
        return new ClothingViewHolder(clothingItemAsView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ClothingRecyclerAdapter.ClothingViewHolder holder, int position) {
        ClothingItem clothingItem = mClothingItems.get(position);
        // no "if empty" because clothingItem always has the resource data member initialized
        Picasso.get()
                .load(clothingItem.getmImageUrl())
                .error(R.drawable.ic_image_placeholder)
                .placeholder(R.drawable.ic_image_placeholder)
                .centerCrop()
                .fit()
                .into(holder.mImageView);
        holder.mImageTitle.setText(clothingItem.getmTitle());
        holder.itemView.setTag(clothingItem); // cardview is holding its clothing item
    }

    @Override
    public int getItemCount() {
        return mClothingItems.size();
    }


}
