package com.minhtzy.moneytracker.entity;

import com.minhtzy.moneytracker.utilities.CurrencyUtils;

import org.parceler.Parcel;

import java.io.Serializable;

@Parcel
public class Money {
    double value;
    CurrencyFormat format;

    public Money() {
    }

    public Money(double value, CurrencyFormat format) {
        this.value = value;
        this.format = format;
    }

    public String toString()
    {
        return CurrencyUtils.getInstance().formatCurrency(String.valueOf(value),format.getCurrencyCode());
    }
}
