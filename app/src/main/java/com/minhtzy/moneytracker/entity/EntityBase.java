package com.minhtzy.moneytracker.entity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;

import com.minhtzy.moneytracker.model.MTDate;

import java.io.Serializable;
import java.util.Date;

public class EntityBase implements IEntity, Serializable {


    ContentValues contentValues;

    public EntityBase() {
        contentValues = new ContentValues();
    }

    public EntityBase(ContentValues contentValues) {
        this.contentValues = contentValues;
    }

    public void loadFromCursor(Cursor c)
    {
        this.contentValues.clear();
        DatabaseUtils.cursorRowToContentValues(c,contentValues);
    }

    protected Boolean getBoolean(String column) {
        return contentValues.getAsBoolean(column);
    }

    protected void setBoolean(String column, Boolean value) {
        contentValues.put(column, value.toString().toUpperCase());
    }

    protected Date getDate(String field) {
        String dateString = getString(field);
        return new MTDate(dateString).toDate();
    }

    protected void setDate(String fieldName, Date value) {
        String dateString = new MTDate(value).toIsoDateString();
        contentValues.put(fieldName, dateString);
    }

    protected Long getLong(String column)
    {
        return contentValues.getAsLong(column);
    }

    protected void setLong(String fieldName, Long value)
    {
        contentValues.put(fieldName,value);
    }

    protected Integer getInt(String column) {
        return contentValues.getAsInteger(column);
    }

    protected void setInt(String fieldName, Integer value) {
        contentValues.put(fieldName, value);
    }

    protected String getString(String fieldName) {
        return contentValues.getAsString(fieldName);
    }

    protected void setString(String fieldName, String value) {
        contentValues.put(fieldName, value);
    }

    protected Double getDouble(String column) {
        return contentValues.getAsDouble(column);
    }

    protected void setDouble(String column, Double value) {
        contentValues.put(column, value);
    }

    @Override
    public ContentValues getContentValues() {
        return this.contentValues;
    }
}
