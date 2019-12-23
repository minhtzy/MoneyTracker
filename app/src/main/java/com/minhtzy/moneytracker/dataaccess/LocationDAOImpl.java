package com.minhtzy.moneytracker.dataaccess;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import com.minhtzy.moneytracker.entity.LocationEntity;

public class LocationDAOImpl implements ILocationDAO {

    public static final String TABLE_LOCACTION_NAME = "tbl_locations";

    private MoneyTrackerDBHelper dbHelper;

    public LocationDAOImpl(Context context) {
        this.dbHelper = new MoneyTrackerDBHelper(context);
    }

    @Override
    public boolean insertLocation(LocationEntity locationEntity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long inserted = db.insert(TABLE_LOCACTION_NAME, null,locationEntity.getContentValues());
        db.close();
        return inserted != -1;
    }

    @Override
    public LocationEntity getLocationById(String id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_LOCACTION_NAME +
                        " WHERE " + LocationEntity.ID  + " = ?";
        Cursor data = db.rawQuery(query,new String[]{id});
        LocationEntity entity = new LocationEntity();

        if(data != null && data.getCount() > 0)
        {
            data.moveToFirst();
            entity.loadFromCursor(data);
        }
        db.close();
        return entity;
    }
}
