package com.benefit.adapters;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.benefit.R;
import com.benefit.ui.Displayable;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for the user's items RecyclerView.
 */
public class DisplayableRecycleAdapter extends RecyclerView.Adapter<DisplayableRecycleAdapter.DisplayViewHolder> {

    private static final int CATEGORIES = 1;
    private static final int PRODUCTS = 2;
    private List<Displayable> displayableItems;
    private OnItemClickListener mListener;
    private int typeOfDisplay;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return mListener;
    }

    static class DisplayViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView mImageTitle;

        DisplayViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            // itemView is the whole card
            super(itemView);
            mImageView = itemView.findViewById(R.id.item_image);
            mImageTitle = itemView.findViewById(R.id.item_title);

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

    public <T extends Displayable> DisplayableRecycleAdapter(List<T> itemList, int typeOfDisplay) {
        displayableItems = new ArrayList<>();
        displayableItems.addAll(itemList);
        this.typeOfDisplay = typeOfDisplay;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public DisplayViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                int viewType) {
        View v = null;
        switch (typeOfDisplay) {
            case CATEGORIES:
                v = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.category_item, parent, false);
                v.setElevation(0);
                break;
            case PRODUCTS:
                v = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.product_item, parent, false);
                v.setElevation(0);
                break;
        }
//        v.findViewById(R.id.item_image_layout).setClipToOutline(true);
        return new DisplayViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DisplayViewHolder holder, int position) {
        Displayable displayableItem = displayableItems.get(position);
        String resource = displayableItem.getImageResource();
        if (resource != null && URLUtil.isValidUrl(resource)) {
            Picasso.get()
                    .load(resource)
                    .placeholder(R.drawable.oval)
                    .error(R.drawable.oval)
                    .centerCrop()
                    .fit()
                    .into(holder.mImageView);
        }
        holder.mImageTitle.setText(displayableItem.getName());
    }

    @Override
    public int getItemCount() {
        return displayableItems.size();
    }


}
