package com.minhtzy.moneytracker.entity;

import android.content.ContentValues;

import com.minhtzy.moneytracker.model.Constants;

import java.io.Serializable;

public class WalletEntity
        extends EntityBase implements Serializable {

    public static final String WALLET_ID = "_id";
    public static final String NAME = "name";
    public static final String INITIAL_BALANCE = "initBalance";
    public static final String WALLET_TYPE = "walletType";
    public static final String CURRENCY_CODE = "currencyCode";
    public static final String USER_ID = "userId";
    public static final String ICON = "icon";
    public static final String CURRENT_BALANCE = "currentBalance";
    // basic wallet
    public static final String NOTE = "note";
    // linker wallet
    public static final String ACCOUNT_NUMBER = "accountNumber";
    public static final String CREDIT_LIMIT = "creditLimit";

    // sync
    public static final String TIME_STAMP = "timestamp";

    public WalletEntity() {
        super();
        setWalletId(Constants.NOT_SET);
        setCurrencyCode("VND");
    }

    public WalletEntity(ContentValues contentValues) {
        super(contentValues);
    }

    public static WalletEntity create (int walletId, String name, double initialBalance, WalletType walletType, String icon,String currencyCode, String userId) {

        WalletEntity wallet = new WalletEntity();
        wallet.setWalletId(walletId);
        wallet.setName(name);
        wallet.setInitialBalance(initialBalance);
        wallet.setWalletType(walletType);
        wallet.setCurrencyCode(currencyCode);
        wallet.setIcon(icon);
        wallet.setUserId(userId);

        return wallet;
    }

    public static WalletEntity create()
    {
        return create(Constants.NOT_SET,"Default Wallet",0,WalletType.BASIC_WALLET,"","VND","");
    }

    public long getWalletId() {
        return getLong(WALLET_ID);
    }

    public String getName() {
        return getString(NAME);
    }

    public double getInitialBalance() {
        return getDouble(INITIAL_BALANCE);
    }

    public WalletType getWalletType() {
        String typeString = getString(WALLET_TYPE);
        return WalletType.from(typeString);
    }

    public String getCurrencyCode() {
        return getString(CURRENCY_CODE);
    }

    public String getUserId() {
        return getString(USER_ID);
    }

    public String getIcon()
    {
        return getString(ICON);
    }

    public String getNote()
    {
        return getString(NOTE);
    }

    public String getAccountNumber()
    {
        return getString(ACCOUNT_NUMBER);
    }

    public Double getCreditLimit()
    {
        return getDouble(CREDIT_LIMIT);
    }

    public Double getCurrentBalance() { return getDouble(CURRENT_BALANCE);}

    public void setWalletId(long walletId) {
        setLong(WALLET_ID,walletId);
    }

    public void setName(String name) {
        setString(NAME,name);
    }

    public void setInitialBalance(double initBalance) {
        setDouble(INITIAL_BALANCE,initBalance);
    }

    public void setWalletType(WalletType walletType) {
        setString(WALLET_TYPE,walletType.getValue());
    }

    public void setCurrencyCode(String currencyCode) {
        setString(CURRENCY_CODE,currencyCode);
    }

    public void setUserId(String userId) {
        setString(USER_ID,userId);
    }

    public void setIcon(String icon)
    {
        setString(ICON,icon);
    }

    public void setNote(String note)
    {
        setString(NOTE,note);
    }

    public void setCreditLimit(double creditLimit)
    {
        setDouble(CREDIT_LIMIT,creditLimit);
    }

    public void setAccountNumber(String accountNumber)
    {
        setString(ACCOUNT_NUMBER,accountNumber);
    }

    public void setCurrentBalance(Double currentBalance)
    {
        setDouble(CURRENT_BALANCE,currentBalance);
    }

    public long getTimestamp()
    {
        return getLong(TIME_STAMP);
    }

    public void setTimeStamp(long timestamp)
    {
        setLong(TIME_STAMP,timestamp);
    }
}
