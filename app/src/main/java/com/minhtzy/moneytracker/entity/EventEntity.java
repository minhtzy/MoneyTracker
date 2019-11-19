package com.minhtzy.moneytracker.entity;

import android.content.ContentValues;

import com.minhtzy.moneytracker.model.Constants;
import com.minhtzy.moneytracker.model.MTDate;

import java.io.Serializable;

public class EventEntity extends EntityBase implements Serializable {

    public static final String EVENT_ID = "_id";
    public static final String EVENT_NAME = "name";
    public static final String EVENT_ICON = "icon";
    public static final String EVENT_TIME_EXPIRE = "timeExpire";


    public EventEntity() {
        super();
        setEventId(Constants.NOT_SET);
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
        return event;
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
}
