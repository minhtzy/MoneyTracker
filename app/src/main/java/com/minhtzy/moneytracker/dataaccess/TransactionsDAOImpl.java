package com.minhtzy.moneytracker.dataaccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.minhtzy.moneytracker.entity.TransactionEntity;
import com.minhtzy.moneytracker.model.DateRange;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TransactionsDAOImpl implements ITransactionsDAO {

    /**
     * Transaction table
     * int transactionId
     * Date transactionDate
     * String transactionNote
     * float moneyTrading
     * CurrencyFormat currency
     * String location
     * int transactionType
     * int wallet
     */

    public static final String TABLE_TRANSACTION_NAME = "tbl_transactions";

    MoneyTrackerDBHelper dbHelper;
    ICategoriesDAO iCategoriesDAO;
    IWalletsDAO iWalletsDAO;

    public TransactionsDAOImpl(Context context) {
        dbHelper = new MoneyTrackerDBHelper(context);
        iCategoriesDAO = new CategoriesDAOImpl(context);
        iWalletsDAO = new WalletsDAOImpl(context);
    }
    // Transaction

    public boolean insertTransaction(TransactionEntity transaction) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        transaction.setTimestamp(com.google.firebase.Timestamp.now().toDate().getTime());
        ContentValues values = transaction.getContentValues();
        long inserted = db.insert(TABLE_TRANSACTION_NAME, TransactionEntity.LOCATION_ID, values);
        db.close();
        return inserted != -1;
    }

    public boolean updateTransaction(TransactionEntity transaction) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        transaction.setTimestamp(com.google.firebase.Timestamp.now().toDate().getTime());
        ContentValues values = new ContentValues();
        int updated = db.update(TABLE_TRANSACTION_NAME,values,TransactionEntity.TRANSACTION_ID + " = ?" ,new String[]{String.valueOf(transaction.getTransactionId())});
        db.close();
        return updated > 0;
    }

    @Override
    public boolean deleteTransaction(TransactionEntity transaction) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deleted = db.delete(TABLE_TRANSACTION_NAME,TransactionEntity.TRANSACTION_ID + " = ?",new String[]{String.valueOf(transaction.getTransactionId())});
        return deleted > 0;
    }

    public TransactionEntity getTransactionById(String transactionId) {
        Cursor data = getTransactionDataById(transactionId);
        if(data != null && data.getCount() > 0) {
            data.moveToFirst();
            return getTransactionFromData(data);
        }
        return null;
    }

    public List<TransactionEntity> getAllTransactionByWalletId(String walletId) {
        Cursor data = getAllTransactionDataByWalletId(walletId);
        List<TransactionEntity> list_result = new ArrayList<>();
        if (data != null && data.getCount() > 0) {
            data.moveToFirst();
            do {
                TransactionEntity transaction = getTransactionFromData(data);
                list_result.add(transaction);
            }
            while (data.moveToNext());
        }
        return list_result;
    }


    @Override
    public List<TransactionEntity> getAllTransactionByPeriod(String walletId, DateRange dateRange) {
        Cursor data = getAllTransactionDataByWalletId(walletId,dateRange);
        List<TransactionEntity> list_result = new ArrayList<>();
        if (data != null && data.getCount() > 0) {
            data.moveToFirst();
            do {
                TransactionEntity transaction = getTransactionFromData(data);
                list_result.add(transaction);
            }
            while (data.moveToNext());
        }
        return list_result;
    }



    private Cursor getAllTransactionDataByWalletId(String walletId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TRANSACTION_NAME +
                " WHERE " + TransactionEntity.TRANSACTION_ID + " = ?" +
                " ORDER BY " + TransactionEntity.TRANSACTION_TIME;
        return db.rawQuery(query, new String[]{String.valueOf(walletId)});
    }
    private Cursor getAllTransactionDataByWalletId(String walletId, DateRange dateRange) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TRANSACTION_NAME +
                " WHERE " + TransactionEntity.TRANSACTION_ID + " = ?" +
                " AND " + TransactionEntity.TRANSACTION_TIME + " >= ?" +
                " AND " + TransactionEntity.TRANSACTION_TIME + " <= ?";
        Cursor cursor = db.rawQuery(query,
                new String[]{
                        String.valueOf(walletId),
                        String.valueOf(dateRange.getDateFrom().toDate().getTime()),
                        String.valueOf(dateRange.getDateTo().toDate().getTime())});
        return cursor;
    }
    public List<TransactionEntity> getAllTransactionDataByDate(long milisStart, long milisEnd){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<TransactionEntity> list = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_TRANSACTION_NAME +
                " WHERE " + TransactionEntity.TRANSACTION_TIME + " >= ?" +
                " AND " + TransactionEntity.TRANSACTION_TIME + " <= ?" ;
        Cursor cursor = db.rawQuery(query,new String[]{
                String.valueOf(milisStart),
                String.valueOf(milisEnd)});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                TransactionEntity transaction = getTransactionFromData(cursor);
                list.add(transaction);
            }
            while (cursor.moveToNext());
        }
        return list;
    }

    public List<TransactionEntity> getAllTransaction() {
        Cursor data = getAllTransactionData();
        List<TransactionEntity> list_result = new ArrayList<>();
        if (data != null && data.getCount() > 0) {
            data.moveToFirst();
            do {
                TransactionEntity transaction = getTransactionFromData(data);
                list_result.add(transaction);
            }
            while (data.moveToNext());
        }
        return list_result;
    }

    private Cursor getAllTransactionData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TRANSACTION_NAME;
        return db.rawQuery(query,null);
    }

    private TransactionEntity getTransactionFromData(Cursor data) {
        TransactionEntity entity = new TransactionEntity();
        entity.loadFromCursor(data);
        return entity;
    }

    private Cursor getTransactionDataById(String transactionId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TRANSACTION_NAME +
                " WHERE " + TransactionEntity.TRANSACTION_ID + " = ?";
        return db.rawQuery(query, new String[]{String.valueOf(transactionId)});
    }

    public List<TransactionEntity> getStatisticalByCategory(int categoryId , int type) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT "
                + " tblt.trading AS trading "
                + " ,tblc._id AS categoryId "
                + " , tblc.type AS type "
                + " FROM tbl_transactions tblt "
                + " INNER JOIN tbl_categories tblc ON tblc._id = tblt.categoryId "
                + " WHERE tblt.categoryId = " + categoryId
                + " AND tblc.type = "+ type;
        List<TransactionEntity> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql,null);

        while(cursor.moveToNext()) {
            TransactionEntity  data= new TransactionEntity();
            data.loadFromCursor(cursor);
            list.add(data);
        }
        return list;
    }
    public List<TransactionEntity> getAllTransactionDataByType(int type){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT "
                + " tblt.trading AS trading "
                + " ,tblc._id AS categoryId "
                + " , tblc.type AS type "
                + " , tblt._id AS _id"
                + " , tblt.transaction_date AS transaction_date"
                + " , tblt.note AS note"
                + " , tblt.currency AS currency"
                + " , tblt.location AS location"
                + " , tblt.walletId AS walletId"
                + " , tblt.media_uri AS media_uri"
                + " FROM tbl_transactions tblt "
                + " INNER JOIN tbl_categories tblc ON tblc._id = tblt.categoryId "
                + " WHERE tblc.type = "+ type;
        List<TransactionEntity> list_result = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql,null);


        if (cursor != null && cursor.getCount() >0){
            cursor.moveToFirst();
            do {
                TransactionEntity transaction = getTransactionFromData(cursor);
                list_result.add(transaction);
            }while (cursor.moveToNext());
        }
        return list_result;
    }

    public List<TransactionEntity> getStatisticalByCategoryInRange(long wallet_id ,int categoryId , DateRange dateRange) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT "
                + " tblt.trading AS trading "
                + " ,tblc._id AS categoryId "
                + " ,tblc.type AS type "
                + " ,tblt.transaction_date as t_date"
                + " FROM tbl_transactions tblt "
                + " INNER JOIN tbl_categories tblc ON tblc._id = tblt.categoryId "
                + " WHERE (tblt.categoryId = " + categoryId + " OR tblc.parentId = " + categoryId +" )"
                + " AND tblt.walletId = "+ wallet_id
                + " AND tblt.transaction_date >= " + dateRange.getDateFrom().getMillis()
                + " AND tblt.transaction_date <= " + dateRange.getDateTo().getMillis();
        List<TransactionEntity> list = new ArrayList<TransactionEntity>();
        Cursor cursor = db.rawQuery(sql,null);

        while(cursor.moveToNext()) {
            TransactionEntity  data= new TransactionEntity();
            data.loadFromCursor(cursor);
            list.add(data);
        }
        return list;
    }


    public List<TransactionEntity> getAllTransactionByCategoryInRange(long wallet_id ,int categoryId , DateRange dateRange) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT *"
                + " FROM tbl_transactions tblt "
                + " INNER JOIN tbl_categories tblc ON tblc._id = tblt.categoryId "
                + " WHERE (tblt.categoryId = " + categoryId + " OR tblc.parentId = " + categoryId +" )"
                + " AND tblt.walletId = "+ wallet_id
                + " AND tblt.transaction_date >= " + dateRange.getDateFrom().getMillis()
                + " AND tblt.transaction_date <= " + dateRange.getDateTo().getMillis();
        List<TransactionEntity> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql,null);

        while(cursor.moveToNext()) {
            list.add(getTransactionFromData(cursor));
        }
        return list;
    }

    public void updateTimeStamp(long transactionId, long timestamp) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "UPDATE " + TABLE_TRANSACTION_NAME
                + " SET " + TransactionEntity.TIMESTAMP + " = " + timestamp
                + " WHERE " + TransactionEntity.TRANSACTION_ID + " = " + transactionId;

        db.execSQL(query);
        db.close();
    }

    @Override
    public List<TransactionEntity> getAllSyncTransaction(String walletId, long timestamp) {
        Cursor data = getAllSyncTransactionData(walletId,timestamp);
        List<TransactionEntity> list_result = new ArrayList<>();
        if (data != null && data.getCount() > 0) {
            data.moveToFirst();
            do {
                TransactionEntity transaction = getTransactionFromData(data);
                list_result.add(transaction);
            }
            while (data.moveToNext());
        }
        return list_result;
    }

    private Cursor getAllSyncTransactionData(String walletId, long timestamp) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TRANSACTION_NAME +
                " WHERE " + TransactionEntity.WALLET_ID + " = ?" +
                " AND (" + TransactionEntity.TIMESTAMP + " > ?" + " OR " + TransactionEntity.TIMESTAMP + " IS NULL)";
        return db.rawQuery(query, new String[]{String.valueOf(walletId),String.valueOf(timestamp)});
    }
}
