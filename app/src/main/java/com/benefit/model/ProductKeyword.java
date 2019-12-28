package com.benefit.model;

public class ProductKeyword {
    private int productId;
    private String keyword;

    public ProductKeyword(int productId, String keyword) {
        this.productId = productId;
        this.keyword = keyword;
    }

    public int getProductId() {
        return productId;
    }

    public String getKeyword() {
        return keyword;
    }
}
