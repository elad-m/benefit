package com.benefit.model;

public class Category {
    private int id;
    private String name;
    private int parentId;
    private int level;
    private boolean isLeaf;

    public Category() {
    }

    public Category(int id, String name, int parentId, int level, boolean isLeaf) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.level = level;
        this.isLeaf = isLeaf;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getParentId() {
        return parentId;
    }

    public int getLevel() {
        return level;
    }

    public boolean isLeaf() {
        return isLeaf;
    }
}
