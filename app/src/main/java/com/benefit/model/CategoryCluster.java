package com.benefit.model;

import com.benefit.ui.Displayable;

import java.io.Serializable;
import java.util.List;

/**
 * A POJO which represents a categories cluster
 */
public class CategoryCluster implements Serializable, Displayable {

    private long id;
    private String name;
    private String type;
    private List<Integer> categoryIdList;
    private String imageResource;

    public CategoryCluster(){}

    public CategoryCluster(String name, String type, List<Integer> categoryIdList) {
        this.name = name;
        this.type = type;
        this.categoryIdList = categoryIdList;
    }

    @Override
    public String getName() {
        return name;
    }

    public long getId(){
        return id;
    }

    public int getIdAsInt() {
        return (int) id;
    }

    public List<Integer> getCategoryIdList() {
        return categoryIdList;
    }

    public String getType() {
        return type;
    }

    @Override
    public String getImageResource(){
        return imageResource;
    }
}
