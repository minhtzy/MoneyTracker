package com.minhtzy.moneytracker.entity;

import android.content.ContentValues;

import com.minhtzy.moneytracker.model.MTDate;

import org.parceler.Parcel;

@Parcel
public class TransactionEntity extends EntityBase implements ITransactionEntity {

    public TransactionEntity() {
        super();
    }

    public TransactionEntity(ContentValues contentValues) {
        super(contentValues);
    }

    public String getMediaUri()
    {
        return getString(MEDIA_URI);
    }

    public long getTransactionId() {
        return getLong(TRANSACTION_ID);
    }

    public double getTransactionAmount() {
        return getDouble(TRANSACTION_AMOUNT);
    }

    public String getTransactionNote() {
        return getString(TRANSACTION_NOTE);
    }

    public int getCategoryId() {
        return getInt(CATEGORY_ID);
    }

    public int getWalletId() {
        return getInt(WALLET_ID);
    }

    public MTDate getTransactionTime() {
        long time = getLong(TRANSACTION_TIME);
        return new MTDate(time);
    }

    public int getEventId() {
        return getInt(EVENT_ID);
    }

    public String getLocationId() {
        return getString(LOCATION_ID);
    }

    public long getTimestamp() {
        return getLong(TIMESTAMP);
    }

    public void setTransactionId(long transactionId) {
        setLong(TRANSACTION_ID,transactionId);
    }

    public void setTransactionAmount(double transactionAmount) {
        setDouble(TRANSACTION_AMOUNT,transactionAmount);
    }

    public void setTransactionNote(String note) {
        setString(TRANSACTION_NOTE,note);
    }

    public void setCategoryId(int categoryId) {
        setInt(CATEGORY_ID,categoryId);
    }

    public void setWalletId(int walletId) {
        setInt(WALLET_ID,walletId);
    }

    public void setTransactionTime(MTDate time) {
        setLong(TRANSACTION_TIME,time.getMillis());
    }

    public void setEventId(int eventId) {
        setInt(EVENT_ID,eventId);
    }

    public void setLocationId(String locationId) {
        setString(LOCATION_ID,locationId);
    }

    public void setMediaUri(String mediaUri)
    {
        setString(MEDIA_URI,mediaUri);
    }

    public void setTimestamp(long timestamp) {setLong(TIMESTAMP,timestamp);}
}
