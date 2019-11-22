package com.minhtzy.moneytracker.dataaccess;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.minhtzy.moneytracker.entity.CurrencyFormat;

import java.util.ArrayList;
import java.util.List;

public class CurrencyFormatDAOImpl implements ICurrencyFormatDAO {

    public static final String TABLE_CURRENCY_FORMAT = "tbl_currency_format";

    private MoneyTrackerDBHelper dbHelper;

    public CurrencyFormatDAOImpl(Context context) {
        dbHelper = new MoneyTrackerDBHelper(context);
    }

    @Override
    public List<CurrencyFormat> getAllCurrencyAvailable() {
        List<CurrencyFormat> currencyFormats = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_CURRENCY_FORMAT;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                CurrencyFormat currencyFormat = new CurrencyFormat();
                currencyFormat.loadFromCursor(cursor);
                currencyFormats.add(currencyFormat);
            }
            while (cursor.moveToNext());
        }
        return currencyFormats;
    }
}
