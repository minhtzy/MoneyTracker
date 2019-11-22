package com.minhtzy.moneytracker.dataaccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.common.Constants;
import com.google.android.gms.common.util.IOUtils;


import java.io.IOException;
import java.io.InputStream;


public class MoneyTrackerDBHelper extends SQLiteOpenHelper {


    public static final String LOG_TAG = "DB_HELPER";
    public static final int DB_VERSION = 1;
    public static final int START_VERSION = 1;
    private Context mContext;

    /**
     * @param context
     */
    public MoneyTrackerDBHelper(Context context) {
        super(context, Constants.DEFAULT_DB_FILENAME, null, DB_VERSION);
        mContext = context;
    }

    public Context getContext(){
        return mContext;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        createDatabase(db);
        createTrigger(db);
        onUpgrade(db,START_VERSION,DB_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // cập nhập cơ sở dữ liệu
        for(int i = oldVersion + 1; i <= newVersion; ++i) {
            // lấy ID của tập tin update trong thư mục raw
            int identifier = mContext.getResources().getIdentifier(
                    String.format("update_database_v%d",i),
                    "raw",
                    mContext.getPackageName());
            if(identifier != 0) executeRawSql(db,identifier);
        }

    }

    private void createDatabase(SQLiteDatabase db) {
        executeRawSql(db, R.raw.money_tracker_database);
    }


    private void executeRawSql(SQLiteDatabase db, int rawId) {
        InputStream inputStream = getContext().getResources().openRawResource(rawId);

        String sqlRaw = "";
        try {

            sqlRaw = new String(IOUtils.toByteArray(inputStream));
        } catch (IOException e) {
            Log.e(LOG_TAG,e.getMessage());
        }

        String sqlStatement[] = sqlRaw.split(";");

        // process all statements
        for (String aSqlStatment : sqlStatement) {
            Log.d(LOG_TAG,aSqlStatment);

            try {
                db.execSQL(aSqlStatment);
            } catch (Exception e) {
                String errorMessage = e.getMessage();
                if (e instanceof SQLiteException && errorMessage != null && errorMessage.contains("not an error (code 0)")) {
                    Log.w(LOG_TAG,errorMessage);
                } else {
                    Log.e(LOG_TAG, "executing raw sql: " + aSqlStatment);
                }
            }
        }
    }


    private void dropDatabase(SQLiteDatabase db) {
        executeRawSql(db, R.raw.drop_money_tracker_database);
    }


    private void createTrigger(SQLiteDatabase db)
    {
        String s1 = "create trigger wallet_insert_transactions after insert on tbl_transactions " +
                    "begin " +
                    "update tbl_wallets " +
                    "set currentBalance = currentBalance + new.amount " +
                    "where _id = new.walletId;" +
                    "end;";

        String s2 = "create trigger wallet_update_transactions after update on tbl_transactions " +
                "begin " +
                "update tbl_wallets " +
                "set currentBalance = currentBalance + new.amount - old.amount " +
                "where _id = new.walletId;" +
                "end;";

        String s3 = "create trigger wallet_delete_transactions after delete on tbl_transactions " +
                "begin " +
                "update tbl_wallets " +
                "set currentBalance = currentBalance - old.amount " +
                "where _id = new.walletId;" +
                "end;";

        try {
            db.execSQL(s1);
            db.execSQL(s2);
            db.execSQL(s3);
        } catch (Exception e) {
            Log.w(LOG_TAG,e.getMessage());
        }
    }

}
