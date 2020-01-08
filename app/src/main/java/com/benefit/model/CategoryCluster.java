package com.benefit.model;

import java.util.List;

/**
 * A POJO which represents a categories cluster for the homepage
 */
public class CategoryCluster {

    private String name;
    private List<Integer> categoryIdList;

    public CategoryCluster(String name, List<Integer> categoryIdList) {
        this.name = name;
        this.categoryIdList = categoryIdList;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getCategoryIdList() {
        return categoryIdList;
    }
}
