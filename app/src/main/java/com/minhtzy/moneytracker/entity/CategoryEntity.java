package com.minhtzy.moneytracker.entity;

import android.content.ContentValues;

import com.minhtzy.moneytracker.model.TransactionTypes;

import java.io.Serializable;

public class CategoryEntity extends EntityBase implements Serializable {

    public static final String CATEGORY_ID = "_id";
    public static final String CATEGORY_TYPE = "type";
    public static final String CATEGORY_NAME = "name";
    public static final String CATEGORY_ICON = "icon";
    public static final String PARENT_ID = "parentId";

    public CategoryEntity() {
    }

    public CategoryEntity(ContentValues contentValues) {
        super(contentValues);
    }

    public int getCategoryId() {
        return getInt(CATEGORY_ID);
    }

    public TransactionTypes getCategoryType() {
        return TransactionTypes.from(getInt(CATEGORY_TYPE));
    }

    public String getCategoryName() {
        return getString(CATEGORY_NAME);
    }

    public String getCategoryIcon() {
        return getString(CATEGORY_ICON);
    }

    public int getParentId() {
        return getInt(PARENT_ID);
    }

    public void setCategoryId(int categoryId) {
        setInt(CATEGORY_ID,categoryId);
    }

    public void setCategoryType(TransactionTypes categoryType) {
        setInt(CATEGORY_TYPE,categoryType.getValue());
    }

    public void setCategoryName(String categoryName) {
        setString(CATEGORY_NAME,categoryName);
    }

    public void getCategoryIcon(String icon) {
        setString(CATEGORY_ICON,icon);
    }

    public void getParentId(int parentId) {
        setInt(PARENT_ID,parentId);
    }

    public float getRate() {
        return getCategoryType().getRate();
    }
}
