package com.minhtzy.moneytracker.model;

import com.minhtzy.moneytracker.entity.CategoryEntity;

import java.util.ArrayList;
import java.util.List;

public class CategoryExpandableGroup {
    private CategoryEntity category;
    private List<CategoryEntity> subCategories;

    public CategoryExpandableGroup() {
        subCategories = new ArrayList<>();
    }

    public CategoryExpandableGroup(CategoryEntity category, List<CategoryEntity> subCategories) {
        this.category = category;
        this.subCategories = subCategories;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public List<CategoryEntity> getSubCategories() {
        return subCategories;
    }

    public int getSubCatCount()
    {
        return subCategories.size();
    }
}
