package com.benefit.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Model POJO for property name.
 */
public class PropertyName {
    private int id;
    private int categoryId;
    private String name;
    private int sortOrder;
    private boolean isMandatory;
    private List<String> validValues;

    public PropertyName(){}

    public PropertyName(int id, int categoryId, String name, int sortOrder, boolean isMandatory, List<String> validValues) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.sortOrder = sortOrder;
        this.isMandatory = isMandatory;
        this.validValues = validValues;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<String> getValidValues() {
        return validValues == null ? null : validValues.stream().sorted().collect(Collectors.toList());
    }
}
