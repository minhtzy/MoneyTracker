package com.minhtzy.moneytracker.dataaccess;

import com.minhtzy.moneytracker.model.Category;

import java.util.List;

public interface ICategoriesDAO {
    public boolean insertCategory(Category category);
    public boolean updateCategory(Category category);
    public boolean deleteCategory(Category category);
    public List<Category> getAllCategory();
    public List<Category> getCategoriesByType(int type);
    public List<Category> getSubCategories(int parentId);
    Category getCategoryById(int typeId);
}
