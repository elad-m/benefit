package com.benefit.model;

/**
 * Model POJO for property value.
 */
public class PropertyValue {
    private String propertyName;
    private String productId;
    private String value;

    public PropertyValue(){}

    public PropertyValue(String propertyName, String productId, String value){
        this.propertyName = propertyName;
        this. productId = productId;
        this.value = value;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getProductId() {
        return productId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
