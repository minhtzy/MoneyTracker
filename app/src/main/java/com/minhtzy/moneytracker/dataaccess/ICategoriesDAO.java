package com.minhtzy.moneytracker.dataaccess;

import com.minhtzy.moneytracker.entity.CategoryEntity;

import java.util.List;

public interface ICategoriesDAO {
    public boolean insertCategory(CategoryEntity category);
    public boolean updateCategory(CategoryEntity category);
    public boolean deleteCategory(CategoryEntity category);
    public List<CategoryEntity> getAllCategory();
    public List<CategoryEntity> getCategoriesByType(int type);
    public List<CategoryEntity> getSubCategories(int parentId);
    CategoryEntity getCategoryById(int typeId);

    CategoryEntity getCategoryByName(String second);
}
