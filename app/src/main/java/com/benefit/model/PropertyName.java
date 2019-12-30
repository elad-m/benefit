package com.benefit.model;

/**
 * Model POJO for property name.
 */
public class PropertyName {
    private int id;
    private int categoryId;
    private String name;
    private int sortOrder;
    private boolean isMandatory;

    public PropertyName(int id, int categoryId, String name, int sortOrder, boolean isMandatory) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.sortOrder = sortOrder;
        this.isMandatory = isMandatory;
    }

    public int getId() {
        return id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public boolean isMandatory() {
        return isMandatory;
    }
}
