package com.minhtzy.moneytracker.utilities;

import android.content.Context;

import com.minhtzy.moneytracker.App;
import com.minhtzy.moneytracker.dataaccess.CategoriesDAOImpl;
import com.minhtzy.moneytracker.dataaccess.ICategoriesDAO;
import com.minhtzy.moneytracker.entity.CategoryEntity;

import java.util.List;

public class CategoryManager {
    private ICategoriesDAO iCategoryDAO;
    private static final CategoryManager ourInstance = new CategoryManager();
    private List<CategoryEntity> allCategory;

    public static CategoryManager getInstance() {
        return ourInstance;
    }

    private void loadCategories() {
        this.iCategoryDAO = new CategoriesDAOImpl(App.self());
        allCategory = iCategoryDAO.getAllCategory();
    }

    private CategoryManager() {
        loadCategories();
    }
    public CategoryEntity getCategoryById(int categoryId) {
        return iCategoryDAO.getCategoryById(categoryId);
    }
}
