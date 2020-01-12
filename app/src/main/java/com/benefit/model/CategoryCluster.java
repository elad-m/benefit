package com.benefit.model;

import java.util.List;

/**
 * A POJO which represents a categories cluster
 */
public class CategoryCluster {

    private String name;
    private String type;
    private List<Integer> categoryIdList;

    public CategoryCluster(String name, String type, List<Integer> categoryIdList) {
        this.name = name;
        this.type = type;
        this.categoryIdList = categoryIdList;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getCategoryIdList() {
        return categoryIdList;
    }

    public String getType() {
        return type;
    }
}
