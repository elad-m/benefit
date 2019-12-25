package com.benefit;

public class ClothingItem {

    private int mImageResource;
    private String mEdit;
    private String mChat;

    public ClothingItem(int imageResource, String edit, String chat){
        mImageResource = imageResource;
        mEdit = edit;
        mChat = chat;
    }


    public int getmImageResource() {
        return mImageResource;
    }

    public void setmImageResource(int mImageResource) {
        this.mImageResource = mImageResource;
    }

    public String getmEdit() {
        return mEdit;
    }

    public void setmEdit(String mEdit) {
        this.mEdit = mEdit;
    }

    public String getmChat() {
        return mChat;
    }

    public void setmChat(String mChat) {
        this.mChat = mChat;
    }
}
