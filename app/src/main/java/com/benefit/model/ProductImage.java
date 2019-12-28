package com.benefit.model;

public class ProductImage {
    private int productId;
    private String imageUrl;

    public ProductImage(int productId, String imageUrl) {
        this.productId = productId;
        this.imageUrl = imageUrl;
    }

    public int getProductId() {
        return productId;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
