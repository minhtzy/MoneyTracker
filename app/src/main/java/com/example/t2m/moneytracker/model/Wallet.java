package com.example.t2m.moneytracker.model;

import java.util.Currency;

public class Wallet {
    public enum WalletType {
        BASIC_WALLET,
        CREDIT_WALLET,
        GOAL_WALLET,
        LINKED_WALLET
    }
    private int walletId;
    private int walletType;
    private String walletName;
    private float currentBalance;
    private int currencyCode;
    private String imageSrc;
    private String userUID;

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public Wallet() {
    }

    public Wallet(int walletId, String walletName, float currentBalance, int currency, int walletType, String imageSrc,String userUID) {
        this.walletId = walletId;
        this.walletName = walletName;
        this.currentBalance = currentBalance;
        this.currencyCode = currency;
        this.walletType = walletType;
        this.imageSrc = imageSrc;
        this.userUID = userUID;
    }

    public Wallet(WalletBuilder builder) {
        this.walletId = builder.walletId;
        this.walletName = builder.walletName;
        this.currentBalance = builder.currentBalance;
        this.currencyCode = builder.currencyCode;
        this.walletType = builder.walletType;
        this.imageSrc = builder.imageSrc;
        this.userUID = builder.userUID;
    }

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public float getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(float currentBalance) {
        this.currentBalance = currentBalance;
    }

    public int getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(int currency) {
        this.currencyCode = currency;
    }

    public int getWalletType() {
        return walletType;
    }

    public void setWalletType(int walletType) {
        this.walletType = walletType;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public static class WalletBuilder {
        private int walletId;
        private String walletName;
        private float currentBalance;
        private int currencyCode;
        private int walletType;
        private String imageSrc;
        private String userUID;

        public WalletBuilder() {
        }

        public WalletBuilder setWalletId(int walletId) {
            this.walletId = walletId;
            return this;
        }

        public WalletBuilder setWalletName(String walletName) {
            this.walletName = walletName;
            return this;
        }

        public WalletBuilder setCurrentBalance(float currentBalance) {
            this.currentBalance = currentBalance;
            return this;
        }

        public WalletBuilder setCurrencyCode(int currency) {
            this.currencyCode = currency;
            return this;
        }

        public WalletBuilder setWalletType(int walletType) {
            this.walletType = walletType;
            return this;
        }

        public WalletBuilder setImageSrc(String imageSrc) {
            this.imageSrc = imageSrc;
            return this;
        }

        public WalletBuilder setUserUID(String userUID) {
            this.userUID = userUID;
            return this;
        }

        public Wallet build() {
            return new Wallet(this);
        }
    }
}
