package com.example.t2m.moneytracker.transaction;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;

public class Transaction {
    enum TransactionType {

    }

    private Date mTransactionDate;
    private String mTransactionNote;
    private String mTransactionType;
    private float mMoneyTrading;
    private Currency currency;

    public Transaction() {
        mTransactionDate = Calendar.getInstance().getTime();
    }

    public Date getmTransactionDate() {
        return mTransactionDate;
    }

    public void setmTransactionDate(Date mTransactionDate) {
        this.mTransactionDate = mTransactionDate;
    }

    public String getmTransactionNote() {
        return mTransactionNote;
    }

    public void setmTransactionNote(String mTransactionNote) {
        this.mTransactionNote = mTransactionNote;
    }

    public float getmMoneyTrading() {
        return mMoneyTrading;
    }

    public void setmMoneyTrading(float mMoneyTrading) {
        this.mMoneyTrading = mMoneyTrading;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getmTransactionType() {
        return mTransactionType;
    }

    public void setmTransactionType(String mTransactionType) {
        this.mTransactionType = mTransactionType;
    }
}
