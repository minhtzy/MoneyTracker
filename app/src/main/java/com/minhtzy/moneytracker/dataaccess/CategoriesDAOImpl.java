package com.minhtzy.moneytracker.dataaccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.minhtzy.moneytracker.entity.CategoryEntity;

import java.util.ArrayList;
import java.util.List;

public class CategoriesDAOImpl implements ICategoriesDAO {

    /**
     * Category table
     * int CategoryId
     * int type
     * String logo
     * String category
     * int parentType
     */

    public static final String TABLE_CATEGORY_NAME = "tbl_categories";

    MoneyTrackerDBHelper dbHelper;
    public CategoriesDAOImpl(Context context) {
        dbHelper = new MoneyTrackerDBHelper(context);
    }

    // Transaction Type DataAccess

    public boolean insertCategory(CategoryEntity category) {
        if (category == null) return false;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = category.getContentValues();
        values.remove(CategoryEntity.CATEGORY_ID);
        int id = (int) db.insert(TABLE_CATEGORY_NAME, CategoryEntity.CATEGORY_ID, values);
        db.close();
        category.setCategoryId(id);
        return id != -1;
    }

    public boolean updateCategory(CategoryEntity category) {
        if (category == null) return false;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = category.getContentValues();
        values.remove(CategoryEntity.CATEGORY_ID);
        int updated = db.update(TABLE_CATEGORY_NAME, values, CategoryEntity.CATEGORY_ID + " = ?", new String[]{String.valueOf(category.getCategoryId())});
        db.close();
        return updated > 0;
    }

    @Override
    public boolean deleteCategory(CategoryEntity category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deleted = db.delete(TABLE_CATEGORY_NAME,CategoryEntity.CATEGORY_ID + " = ?",new String[]{String.valueOf(category.getCategoryId())});
        return deleted > 0;
    }

    public CategoryEntity getCategoryById(int id) {
        Cursor data = getCategoryDataById(id);
        if (data != null && data.getCount() > 0) {
            data.moveToFirst();
            CategoryEntity category = getCategoryFromData(data);
            return category;
        } else {
            return null;
        }
    }

    public List<CategoryEntity> getAllCategory() {
        Cursor data = getAllCategoryData();
        List<CategoryEntity> list_result = new ArrayList<>();
        if (data != null && data.getCount() > 0) {
            data.moveToFirst();
            do {
                CategoryEntity category = getCategoryFromData(data);
                list_result.add(category);
            }
            while (data.moveToNext());
        }
        return list_result;
    }

    public List<CategoryEntity> getCategoriesByType(int type) {
        Cursor data = getAllCategoryDataByType(type);
        List<CategoryEntity> list_result = new ArrayList<>();
        if (data != null && data.getCount() > 0) {
            data.moveToFirst();
            do {
                CategoryEntity category = getCategoryFromData(data);
                list_result.add(category);
            }
            while (data.moveToNext());
        }
        return list_result;
    }

    private Cursor getAllCategoryDataByType(int type) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CATEGORY_NAME +
                " WHERE " + CategoryEntity.PARENT_ID + " IS NULL " +
                " AND " + CategoryEntity.CATEGORY_TYPE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(type)});
        return cursor;
    }

    public List<CategoryEntity> getSubCategories(int parentId) {
        Cursor data = getSubCategoriesData(parentId);
        List<CategoryEntity> list_result = new ArrayList<>();
        if (data != null && data.getCount() > 0) {
            data.moveToFirst();
            do {
                CategoryEntity category = getCategoryFromData(data);
                list_result.add(category);
            }
            while (data.moveToNext());
        }
        return list_result;
    }

    private Cursor getSubCategoriesData(int parentId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CATEGORY_NAME +
                " WHERE " + CategoryEntity.PARENT_ID+ " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(parentId)});
        return cursor;
    }

    private CategoryEntity getCategoryFromData(Cursor data) {
        CategoryEntity category = new CategoryEntity();
        category.loadFromCursor(data);
        return category;
    }

    private Cursor getCategoryDataById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CATEGORY_NAME +
                " WHERE " + CategoryEntity.CATEGORY_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});
        return cursor;
    }

    private Cursor getAllCategoryData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CATEGORY_NAME +
                " WHERE " + CategoryEntity.PARENT_ID + " IS NULL";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public int getCountCategoryParent(int type) {
        List<CategoryEntity> list = getCategoriesByType(type);
        int count = 0;
        for (CategoryEntity c : list) {
            if (c.getCategoryId() > 0) {
                count = count + 1;
            }
        }
        return count;
    }
}
