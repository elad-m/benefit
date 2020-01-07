package com.benefit.services;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.benefit.drivers.DatabaseDriver;
import com.benefit.model.Category;
import com.benefit.model.PropertyName;
import com.google.firebase.firestore.CollectionReference;

import java.util.List;

/**
 * A service which responsible for categories' data
 */
public class CategoryService extends ViewModel {
    private DatabaseDriver databaseDriver;
    private CollectionReference categoriesCollection;
    private static final String COLLECTION_NAME_CATEGORY = "categories";
    private static final String COLLECTION_NAME_PROPERTY_NAME = "property_name";

    public CategoryService(DatabaseDriver databaseDriver) {
        this.databaseDriver = databaseDriver;
        this.categoriesCollection = this.databaseDriver.getCollectionByName("categories");
    }

    public void addCategory(Category category) {
        this.categoriesCollection.add(category);
    }

    public MutableLiveData<Category> getCategoryById(int categoryId) {
        return this.databaseDriver.getSingleDocumentByField(
                COLLECTION_NAME_CATEGORY, "id", categoryId, Category.class);
    }

    public MutableLiveData<List<Category>> getAllMetaCategories() {
        return getCategoriesByField("level", 0);
    }

    public MutableLiveData<List<Category>> getChildrenByParentId(int parentId) {
        return getCategoriesByField("parentId", parentId);
    }

    public MutableLiveData<List<PropertyName>> getAllPropertiesByCategoryId(int categoryId) {
        return this.databaseDriver.getDocumentsByField(
                COLLECTION_NAME_PROPERTY_NAME, "categoryId", categoryId, PropertyName.class);
    }

    private MutableLiveData<List<Category>> getCategoriesByField(String fieldName, Object fieldValue) {
        return this.databaseDriver.getDocumentsByField(COLLECTION_NAME_CATEGORY, fieldName, fieldValue, Category.class);
    }
}
