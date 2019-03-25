package com.example.t2m.moneytracker.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class TransactionType implements Serializable {
    private int id;
    private int type;
    private String icon;
    private String category;
    private TransactionType parentType;


    public static final int TRANSACTION_TYPE_EXPENSE = 1;
    public static final int TRANSACTION_TYPE_INCOME = 2;
    public static final int TRANSACTION_TYPE_DEBIT = 3;
    public static final int TRANSACTION_TYPE_LOAN = 4;
    public TransactionType() {
    }

    public TransactionType(int id, int type, String category, String icon, TransactionType parentType) {
        this.id = id;
        this.type = type;
        this.icon = icon;
        this.category = category;
        this.parentType = parentType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public TransactionType getParentType() {
        return parentType;
    }

    public void setParentType(TransactionType parentType) {
        this.parentType = parentType;
    }

}
