package com.benefit.model;

import com.benefit.ui.Displayable;

import java.io.Serializable;

/**
 * Model POJO for category of products.
 */
public class Category implements Serializable, Displayable {
    private long id;
    private String name;
    private int parentId;
    private int level;
    private boolean isLeaf;
    private String imageResource;

    public Category() {
    }

    public Category(int id, String name, int parentId, int level, boolean isLeaf) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.level = level;
        this.isLeaf = isLeaf;
    }

    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getParentId() {
        return parentId;
    }

    public int getLevel() {
        return level;
    }

    public boolean getIsLeaf() {
        return isLeaf;
    }

    @Override
    public String getImageResource(){
        return imageResource;
    }
}
