package com.example.t2m.moneytracker.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;

public class Transaction implements Serializable {

    private int transactionId;
    private Date transactionDate;
    private String transactionNote;
    private float moneyTrading;
    private String currencyCode;
    private String location;
    private TransactionType transactionType;
    private Category category;
    Wallet wallet;

    public Transaction() {
        this.transactionDate = Calendar.getInstance().getTime();
        transactionType = new TransactionType();
    }

    public Transaction(TransactionBuilder builder) {
        this.transactionId = builder.transactionId;
        this.transactionDate = builder.transactionDate;
        this.transactionNote = builder.transactionNote;
        this.moneyTrading = builder.moneyTrading;
        this.currencyCode = builder.currencyCode;
        this.location = builder.location;
        this.transactionType = builder.transactionType;
        this.wallet = builder.wallet;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionNote() {
        return transactionNote;
    }

    public void setTransactionNote(String transactionNote) {
        this.transactionNote = transactionNote;
    }

    public float getMoneyTrading() {
        int type = category.getType().getValue();

        if(type == 1 || type == 3) {
            return -1 * Math.abs(moneyTrading);
        }
        else {
            return Math.abs(moneyTrading);
        }

    }

    public void setMoneyTrading(float moneyTrading) {
        this.moneyTrading = moneyTrading;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyId(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public static class TransactionBuilder {
        int transactionId;
        private Date transactionDate;
        private String transactionNote;
        private float moneyTrading;
        private String currencyCode;
        private String location;
        private TransactionType transactionType;
        Wallet wallet;

        public TransactionBuilder() {
        }

        public TransactionBuilder setTransactionId(int transactionId) {
            this.transactionId = transactionId;
            return this;
        }
        public TransactionBuilder setTransactionDate(Date transactionDate) {
            this.transactionDate = transactionDate;
            return this;
        }

        public TransactionBuilder setTransactionNote(String transactionNote) {
            this.transactionNote = transactionNote;
            return this;
        }

        public TransactionBuilder setMoneyTrading(float moneyTrading) {
            this.moneyTrading = moneyTrading;
            return this;
        }

        public TransactionBuilder setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
            return this;
        }

        public TransactionBuilder setLocation(String location) {
            this.location = location;
            return this;
        }

        public TransactionBuilder setTransactionType(TransactionType transactionType) {
            this.transactionType = transactionType;
            return this;
        }

        public TransactionBuilder setWallet(Wallet wallet) {
            this.wallet = wallet;
            return this;
        }

        public Transaction build() {
            return new Transaction(this);
        }
    }

}
