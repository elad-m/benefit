package com.benefit.ui.profile;

/**
 * Represents a ui component of an item: the rounded rectangle with a photo and title.
 * Used in the RecyclerView as the object of the CardView.
 */
public class ClothingItem {


    private int mProductId;
    private String mImageUrl;
    private String mTitle;

    public ClothingItem() {
    }


    public ClothingItem(String imageUrl, String title, int id) {
        mImageUrl = imageUrl;
        mTitle = title;
        mProductId = id;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String imageUrl) {
        this.mImageUrl = imageUrl;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getmProductId() {
        return mProductId;
    }

    public void setmProductId(int mProductId) {
        this.mProductId = mProductId;
    }
}
