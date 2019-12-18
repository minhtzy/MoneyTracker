package com.minhtzy.moneytracker.entity;

import android.content.ContentValues;

import com.minhtzy.moneytracker.model.Constants;
import com.minhtzy.moneytracker.model.EventStatus;
import com.minhtzy.moneytracker.model.MTDate;

import org.parceler.Parcel;

import java.io.Serializable;

@Parcel
public class EventEntity extends EntityBase {

    public static final String EVENT_ID = "_id";
    public static final String EVENT_NAME = "name";
    public static final String EVENT_ICON = "icon";
    public static final String EVENT_TIME_EXPIRE = "timeExpire";
    public static final String EVENT_STATUS = "status";
    public static final String CURRENCY_CODE = "currencyCode";
    public static final String LOCK_WALLET = "lockWallet";
    public static final String SPENT_AMOUNT = "spent";

    public EventEntity() {
        super();
        setEventId(Constants.NOT_SET);
        setStatus(EventStatus.START);
    }

    public EventEntity(ContentValues contentValues) {
        super(contentValues);
    }

    public static EventEntity create()
    {
        return create(Constants.NOT_SET,"","",new MTDate());
    }

    public static EventEntity create(int eventId,String eventName,String eventIcon,MTDate timeExpire)
    {
        EventEntity event = new EventEntity();
        event.setEventId(eventId);
        event.setEventName(eventName);
        event.setEventIcon(eventIcon);
        event.setTimeExpire(timeExpire);
        event.setStatus(EventStatus.START);
        return event;
    }

    public void setStatus(EventStatus start) {
        setString(EVENT_STATUS,start.getValue());
    }

    public EventStatus getStatus()
    {
        return EventStatus.from(getString(EVENT_STATUS));
    }


    public int getEventId() {
        return getInt(EVENT_ID);
    }

    public String getEventName() {
        return getString(EVENT_NAME);
    }

    public String getEventIcon() {
        return getString(EVENT_ICON);
    }

    public MTDate getTimeExpire() {
        long time = getLong(EVENT_TIME_EXPIRE);
        return new MTDate(time);
    }

    public void setEventId(int eventId) {
        setInt(EVENT_ID,eventId);
    }

    public void setEventName(String eventName) {
        setString(EVENT_NAME,eventName);
    }

    public void setEventIcon(String eventIcon) {
        setString(EVENT_ICON,eventIcon);
    }

    public void setTimeExpire(MTDate timeExpire) {
        setLong(EVENT_TIME_EXPIRE,timeExpire.getMillis());
    }

    public double getSpentAmount() {
        return getDouble(SPENT_AMOUNT);
    }

    public void setSpentAmount(double spent)
    {
        setDouble(SPENT_AMOUNT,spent);
    }

    public void setLockWallet(String walletId) {
        setString(LOCK_WALLET,walletId);
    }

    public String getLockWallet() {
        return getString(LOCK_WALLET);
    }

    public String getCurrencyCode() {
        return getString(CURRENCY_CODE);
    }

    public void setCurrencyCode(String currencyCode) {
        setString(CURRENCY_CODE,currencyCode);
    }
}
