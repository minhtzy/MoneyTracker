package com.minhtzy.moneytracker.dataaccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.minhtzy.moneytracker.entity.EventEntity;
import com.minhtzy.moneytracker.model.EventStatus;

import java.util.ArrayList;
import java.util.List;

public class EventDAOImpl implements IEventDAO {

    String TABLE_EVENT_NAME = "tbl_events";
    MoneyTrackerDBHelper dbHelper;

    public EventDAOImpl(Context context) {
        dbHelper = new MoneyTrackerDBHelper(context);
    }

    @Override
    public List<EventEntity> getAllAvailableEvent(EventStatus eventStatus) {
        List<EventEntity> eventEntities = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_EVENT_NAME + " WHERE " +EventEntity.EVENT_STATUS + " = ?";
        Cursor cursor = db.rawQuery(query,new String[]{eventStatus.getValue()});
        if(cursor!= null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do {
                EventEntity event = new EventEntity();
                event.loadFromCursor(cursor);
                updateEventSpent(event);
                eventEntities.add(event);
            }while (cursor.moveToNext());
        }
        db.close();
        return eventEntities;
    }

    private void updateEventSpent(EventEntity event) {
        String query = "SELECT SUM(amount) " +
                        "FROM tbl_transactions " +
                        " WHERE eventId = ?";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,new String[]{String.valueOf(event.getEventId())});
        if(cursor != null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            float amount = cursor.getFloat(0);
            event.setSpentAmount(amount);
        }
        db.close();
    }

    @Override
    public boolean insert(EventEntity entity) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        entity.getContentValues().remove(EventEntity.EVENT_ID);
        long inserted = db.insert(TABLE_EVENT_NAME,EventEntity.LOCK_WALLET,entity.getContentValues());
        entity.setEventId((int)inserted);
        db.close();
        return inserted != -1;
    }

    @Override
    public boolean updateStatus(int eventId, EventStatus eventStatus) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(EventEntity.EVENT_STATUS,eventStatus.getValue());
        int updated = db.update(TABLE_EVENT_NAME,values,EventEntity.EVENT_ID + " = ? ",new String[]{String.valueOf(eventId)});
        db.close();
        return updated > 0;
    }

    @Override
    public List<EventEntity> getAllAvailableEventForWallet(String mWalletId) {
        List<EventEntity> eventEntities = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_EVENT_NAME +
                " WHERE " + EventEntity.LOCK_WALLET + " = ?" +
                " OR " + EventEntity.LOCK_WALLET + " IS NULL" +
                " OR " + EventEntity.LOCK_WALLET + " = ''";
        Cursor cursor = db.rawQuery(query,new String[]{mWalletId});
        if(cursor!= null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do {
                EventEntity event = new EventEntity();
                event.loadFromCursor(cursor);
                updateEventSpent(event);
                eventEntities.add(event);
            }while (cursor.moveToNext());
        }
        db.close();
        return eventEntities;
    }


    public void deleteEvent(int eventId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_EVENT_NAME,"_id = ?",new String[]{String.valueOf(eventId)});
        db.close();
    }

    public EventEntity getEventById(int eventId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_EVENT_NAME +
                " WHERE " + EventEntity.EVENT_ID + " = ?";
        Cursor cursor = db.rawQuery(query,new String[]{String.valueOf(eventId)});
        if(cursor != null && cursor.getCount() > 0)
        {

            cursor.moveToFirst();

            EventEntity event = new EventEntity();
            event.loadFromCursor(cursor);
            updateEventSpent(event);
            return  event;
        }
        db.close();
        return null;
    }
}
