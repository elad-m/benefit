package com.benefit;

public class ClothingItem {

    private int mImageResource;
    private String mTitle;

    ClothingItem() {
    }

    ClothingItem(int imageResource, String title) {
        mImageResource = imageResource;
        mTitle = title;
    }


    int getmImageResource() {
        return mImageResource;
    }

    public void setmImageResource(int mImageResource) {
        this.mImageResource = mImageResource;
    }


    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}
