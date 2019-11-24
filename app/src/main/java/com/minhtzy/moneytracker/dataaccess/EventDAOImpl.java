package com.minhtzy.moneytracker.dataaccess;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
                eventEntities.add(event);
            }while (cursor.moveToNext());
        }
        return eventEntities;
    }

    @Override
    public boolean insert(EventEntity entity) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        entity.getContentValues().remove(EventEntity.EVENT_ID);
        long inserted = db.insert(TABLE_EVENT_NAME,EventEntity.LOCK_WALLET,entity.getContentValues());
        entity.setEventId((int)inserted);
        return inserted != -1;
    }
}
