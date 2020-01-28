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
import com.benefit.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Adapter for the user's products RecyclerView.
 */
public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.ProductViewHolder> {
    private ArrayList<Product> mProducts;
    private OnItemClickListener mListener;
    private RecyclerView mRecyclerView;

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
        void onDeleteClick(int posotion, View view);
        void onEditClick(int position, View view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return mListener;
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView mImageTitle;
        Button mDeleteButton;
        Button mEditButton;

        ProductViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
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
                public void onClick(View cardViewClicked) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position, cardViewClicked);
                        }
                    }
                }
            });
        }
    }

    public ProductRecyclerAdapter(ArrayList<Product> itemList) {
        mProducts = itemList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                int viewType) {
        View clothingItemAsView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.product_view_holder_layout, parent, false);  // TODO OOOOOOOOOOOO
        clothingItemAsView.findViewById(R.id.image_layout).setClipToOutline(true);
        mRecyclerView = (RecyclerView) parent;
        return new ProductViewHolder(clothingItemAsView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = mProducts.get(position);
        // no "if empty" because clothingItem always has the resource data member initialized
        Picasso.get()
                .load(product.getImagesUrls().get(0))
                .centerCrop()
                .fit()
                .into(holder.mImageView);
        holder.mImageTitle.setText(product.getTitle());
        holder.itemView.setTag(product); // cardview is holding its clothing item
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }


}
