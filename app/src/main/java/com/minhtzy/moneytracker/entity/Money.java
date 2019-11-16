package com.minhtzy.moneytracker.entity;

import com.minhtzy.moneytracker.utilities.CurrencyUtils;

import org.parceler.Parcel;

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
        return CurrencyUtils.formatCurrency(String.valueOf(value),format);
    }
}
