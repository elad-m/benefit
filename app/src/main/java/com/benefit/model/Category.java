package com.benefit.model;

/**
 *  Model POJO for category.
 */
public class Category {
    private String name;
    private String parentId;
    private boolean isLeaf;

    public Category(){}

    public Category(String name, String parentId, boolean isLeaf){
        this.name = name;
        this.parentId = parentId;
        this.isLeaf = isLeaf;
    }

    public String getName() {
        return name;
    }

    public String getParentId() {
        return parentId;
    }

    public boolean isLeaf() {
        return isLeaf;
    }
}
