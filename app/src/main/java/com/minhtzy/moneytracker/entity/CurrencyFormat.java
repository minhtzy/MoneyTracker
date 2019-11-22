package com.minhtzy.moneytracker.entity;

import android.content.ContentValues;

import org.parceler.Parcel;

import java.io.Serializable;

@Parcel
public class CurrencyFormat extends EntityBase {
    public static final String CURRENCY_ID = "_id";
    public static final String CURRENCY_NAME = "name";
    public static final String CURRENCY_SYMBOL = "currencySymbol";
    public static final String GROUP_SEPARATOR = "GROUP_SEPARATOR";
    public static final String DECIMAL_POINT = "decimalPoint";
    public static final String CURRENCY_CODE = "currencyCode";

    public CurrencyFormat() {
        super();
    }

    public CurrencyFormat(ContentValues contentValues) {
        super(contentValues);
    }

    public String getCurrencyId() {
        return getString(CURRENCY_ID);
    }

    public String getCurrencyName() {
        return getString(CURRENCY_NAME);
    }

    public String getCurrencyCode() {
        return getString(CURRENCY_CODE);
    }

    public String getDecimalPoint() {
        return getString(DECIMAL_POINT);
    }

    public String getCurrencySymbol() {
        return getString(CURRENCY_SYMBOL);
    }

    public String getGroupSeparator()
    {
        return getString(GROUP_SEPARATOR);
    }

    public void setGroupSeperator(String groupSeperator)
    {
        setString(GROUP_SEPARATOR,groupSeperator);
    }

    public void setCurrencyId(String currencyId) {
        setString( CURRENCY_ID,currencyId);
    }

    public void setCurrencyName(String currencyName) {
        setString( CURRENCY_NAME,currencyName);
    }

    public void setCurrencyCode(String pfxSymbol) {
        setString(CURRENCY_CODE,pfxSymbol);
    }

    public void setDecimalPoint(String decimalPoint) {
        setString(DECIMAL_POINT,decimalPoint);
    }

    public void setCurrencySymbol(String currencySymbol) {
        setString(CURRENCY_SYMBOL,currencySymbol);
    }
}
