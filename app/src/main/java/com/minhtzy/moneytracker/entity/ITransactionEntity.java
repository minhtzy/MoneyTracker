package com.minhtzy.moneytracker.entity;

import com.minhtzy.moneytracker.model.MTDate;

import java.util.Date;

public interface ITransactionEntity {

    public static final String TRANSACTION_ID = "_id";
    public static final String TRANSACTION_AMOUNT = "amount";
    public static final String TRANSACTION_NOTE = "note";
    public static final String MEDIA_URI = "mediaUri";
    public static final String CATEGORY_ID = "categoryId";
    public static final String WALLET_ID = "walletId";
    public static final String TRANSACTION_TIME = "time";
    public static final String EVENT_ID = "eventId";
    public static final String LOCATION_ID = "locationId";

    public static final String TIMESTAMP = "timestamp";

    String getMediaUri();

    String getTransactionId();

    double getTransactionAmount();

    String getTransactionNote();

    int getCategoryId();

    String getWalletId();

    Date getTransactionTime() ;

    int getEventId();

    String getLocationId();

    long getTimestamp();

    void setTransactionId(String transactionId);

    void setTransactionAmount(double transactionAmount);

    void setTransactionNote(String note);

    void setCategoryId(int categoryId);

    void setWalletId(String walletId);

    void setTransactionTime(MTDate time);

    void setEventId(int eventId);

    void setLocationId(String locationId);

    void setMediaUri(String mediaUri);

    void setTimestamp(long timestamp);
}
