package com.benefit.model;

/**
 * Model POJO for property value.
 */
public class PropertyValue {
    private int id;
    private int propertyNameId;
    private int productId;
    private String value;

    public PropertyValue(int id, int propertyNameId, int productId, String value) {
        this.id = id;
        this.propertyNameId = propertyNameId;
        this.productId = productId;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public int getPropertyNameId() {
        return propertyNameId;
    }

    public int getProductId() {
        return productId;
    }

    public String getValue() {
        return value;
    }
}
