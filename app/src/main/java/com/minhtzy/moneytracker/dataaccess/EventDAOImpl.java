package com.minhtzy.moneytracker.dataaccess;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.minhtzy.moneytracker.entity.EventEntity;
import com.minhtzy.moneytracker.event.EventFragment;

import java.util.ArrayList;
import java.util.List;

public class EventDAOImpl implements IEventDAO {

    String TABLE_EVENT_NAME = "tbl_events";
    MoneyTrackerDBHelper dbHelper;

    public EventDAOImpl(Context context) {
        dbHelper = new MoneyTrackerDBHelper(context);
    }

    @Override
    public List<EventEntity> getAllAvaialbleEvent() {

        List<EventEntity> eventEntities = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_EVENT_NAME;
        Cursor cusor = db.rawQuery(query,null);
        if(cusor!= null && cusor.getCount() > 0)
        {
            cusor.moveToFirst();
            do {
                EventEntity event = new EventEntity();
                event.loadFromCursor(cusor);
                eventEntities.add(event);
            }while (cusor.moveToNext());
        }
        return eventEntities;
    }
}
