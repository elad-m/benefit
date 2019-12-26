package com.benefit.model;

/**
 * Model POJO for property name.
 */
public class PropertyName {
    private String categoryId;
    private String name;
    private String sortOrder;
    private boolean isMandatory;

    public PropertyName(){}

    public PropertyName(String categoryId, String name){
        this.categoryId = categoryId;
        this.name = name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void setMandatory(boolean mandatory) {
        isMandatory = mandatory;
    }
}
