package com.example.t2m.moneytracker.dataaccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.t2m.moneytracker.model.Transaction;
import com.example.t2m.moneytracker.model.TransactionType;
import com.example.t2m.moneytracker.model.Wallet;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class MoneyTrackerDBHelper extends SQLiteOpenHelper {


    public static final String DB_FILE_NAME = "money_tracker.db";
    public static final int DB_VERSION = 1;

    /**
     * TransactionType table
     * int transactionTypeId
     * int type
     * String logo
     * String category
     * int parentType
     */

    public static final String TABLE_TRANSACTION_TYPE_NAME = "tbl_transaction_type";
    public static final String COLUMN_TRANSACTION_TYPE_ID = "_id";
    public static final String COLUMN_TRANSACTION_TYPE_TYPE = "type";
    public static final String COLUMN_TRANSACTION_TYPE_ICON = "icon";
    public static final String COLUMN_TRANSACTION_TYPE_CATEGORY = "category";
    public static final String COLUMN_TRANSACTION_TYPE_PARENT_ID = "parentId";


    /**
     * Transaction table
     * int transactionId
     * Date transactionDate
     * String transactionNote
     * float moneyTrading
     * Currency currency
     * String location
     * int transactionType
     * int wallet
     */

    public static final String TABLE_TRANSACTION_NAME = "tbl_transaction";
    public static final String COLUMN_TRANSACTION_ID = "_id";
    public static final String COLUMN_TRANSACTION_DATE = "date";
    public static final String COLUMN_TRANSACTION_NOTE = "note";
    public static final String COLUMN_TRANSACTION_TRADING = "trading";
    public static final String COLUMN_TRANSACTION_CURRENCY = "currency";
    public static final String COLUMN_TRANSACTION_LOCATION = "location";
    public static final String COLUMN_TRANSACTION_TYPE_ID_FK = "typeId";
    public static final String COLUMN_WALLET_ID_FK = "walletId";


    /**
     * Wallet table
     * int walletId
     * String walletName;
     * float currentBalance;
     * Currency currency;
     * int walletType;
     * String imageSrc;
     * String userUID;
     */

    public static final String TABLE_WALLET_NAME = "tbl_wallet";
    public static final String COLUMN_WALLET_ID = "_id";
    public static final String COLUMN_WALLET_NAME = "name";
    public static final String COLUMN_WALLET_BALANCE = "balance";
    public static final String COLUMN_WALLET_CURRENCY = "currency";
    public static final String COLUMN_WALLET_TYPE = "walletType";
    public static final String COLUMN_WALLET_ICON = "icon";
    public static final String COLUMN_WALLET_USER_ID = "userId";


    /**
     * @param context
     */
    public MoneyTrackerDBHelper(Context context) {
        super(context, DB_FILE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDatabase(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            dropDatabase(db);
            onCreate(db);
        }
    }

    private void createDatabase(SQLiteDatabase db) {
        String CREATE_WALLET_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_WALLET_NAME +
                "(" +
                COLUMN_WALLET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_WALLET_NAME + " TEXT NOT NULL, " +
                COLUMN_WALLET_BALANCE + " REAL NOT NULL, " +
                COLUMN_WALLET_CURRENCY + " TEXT NOT NULL, " +
                COLUMN_WALLET_TYPE + " INTEGER, " +
                COLUMN_WALLET_ICON + " INTEGER, " +
                COLUMN_WALLET_USER_ID + " TEXT " +
                ");";
        String CREATE_TRANSACTION_TYPE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TRANSACTION_TYPE_NAME +
                "(" +
                COLUMN_TRANSACTION_TYPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TRANSACTION_TYPE_TYPE + " INTEGER NOT NULL, " +
                COLUMN_TRANSACTION_TYPE_CATEGORY + " TEXT NOT NULL, " +
                COLUMN_TRANSACTION_TYPE_ICON + " TEXT NOT NULL, " +
                COLUMN_TRANSACTION_TYPE_PARENT_ID + " INTEGER, " +
                "CONSTRAINT fk_transaction_parent_id " +
                "FOREIGN KEY (" + COLUMN_TRANSACTION_TYPE_PARENT_ID + ")" +
                "REFERENCES " + TABLE_TRANSACTION_TYPE_NAME + "(" + COLUMN_TRANSACTION_TYPE_ID + ")" +
                " ON UPDATE CASCADE ON DELETE CASCADE" +
                ");";

        String CREATE_TRANSACTION_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TRANSACTION_NAME +
                "(" +
                COLUMN_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TRANSACTION_CURRENCY + " TEXT NOT NULL, " +
                COLUMN_TRANSACTION_TRADING + " REAL NOT NULL, " +
                COLUMN_TRANSACTION_DATE + " INTEGER NOT NULL, " +
                COLUMN_TRANSACTION_NOTE + " TEXT, " +
                COLUMN_TRANSACTION_LOCATION + " TEXT, " +
                COLUMN_TRANSACTION_TYPE_ID_FK + " INTEGER NOT NULL, " +
                COLUMN_WALLET_ID_FK + " INTEGER NOT NULL, " +
                "CONSTRAINT fk_transaction_type_id " +
                "FOREIGN KEY (" + COLUMN_TRANSACTION_TYPE_ID_FK + ")" +
                "REFERENCES " + TABLE_TRANSACTION_TYPE_NAME + "(" + COLUMN_TRANSACTION_TYPE_ID + ")" +
                " ON UPDATE CASCADE ON DELETE CASCADE , " +
                "CONSTRAINT fk_transaction_wallet_id " +
                "FOREIGN KEY (" + COLUMN_WALLET_ID_FK + ")" +
                "REFERENCES " + TABLE_WALLET_NAME + "(" + COLUMN_WALLET_ID + ")" +
                " ON UPDATE CASCADE ON DELETE CASCADE" +
                ");";
        db.execSQL(CREATE_WALLET_TABLE);
        db.execSQL(CREATE_TRANSACTION_TYPE_TABLE);
        db.execSQL(CREATE_TRANSACTION_TABLE);
    }

    private void dropDatabase(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = OFF;");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WALLET_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION_TYPE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION_NAME);
        db.execSQL("PRAGMA foreign_keys = ON;");
    }


    // Wallet data access

    public Wallet getWalletById(int id) {
        Cursor data = getWalletDataById(id);
        if (data != null && data.getCount() > 0) {
            data.moveToFirst();
            Wallet wallet = getWalletFromData(data);
            return wallet;
        } else {
            return null;
        }
    }

    public List<Wallet> getAllWalletByUser(String userId) {
        Cursor data = getWalletDataByUser(userId);
        List<Wallet> list_wallet = new ArrayList<>();
        if (data != null && data.getCount() > 0) {
            data.moveToFirst();
            do {
                Wallet wallet = getWalletFromData(data);
                list_wallet.add(wallet);
            }
            while (data.moveToNext());
        }
        return list_wallet;
    }

    public boolean insertWallet(Wallet wallet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_WALLET_NAME, wallet.getWalletName());
        values.put(COLUMN_WALLET_BALANCE, wallet.getCurrentBalance());
        values.put(COLUMN_WALLET_CURRENCY, wallet.getCurrencyCode());
        values.put(COLUMN_WALLET_TYPE, wallet.getWalletType());
        values.put(COLUMN_WALLET_ICON, wallet.getImageSrc());
        values.put(COLUMN_WALLET_USER_ID, wallet.getUserUID());
        int id = (int) db.insert(TABLE_WALLET_NAME, null, values);
        db.close();
        if (id == -1) return false;
        wallet.setWalletId(id);
        return true;
    }

    public void updateWallet(Wallet wallet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_WALLET_NAME, wallet.getWalletName());
        values.put(COLUMN_WALLET_BALANCE, wallet.getCurrentBalance());
        values.put(COLUMN_WALLET_CURRENCY, wallet.getCurrencyCode());
        values.put(COLUMN_WALLET_TYPE, wallet.getWalletType());
        values.put(COLUMN_WALLET_ICON, wallet.getImageSrc());
        values.put(COLUMN_WALLET_USER_ID, wallet.getUserUID());
        db.update(TABLE_WALLET_NAME, values, COLUMN_WALLET_ID + " = ?", new String[]{String.valueOf(wallet.getWalletId())});
        db.close();
    }

    public void deleteWallet(int walletId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WALLET_NAME, COLUMN_WALLET_ID + " = ?", new String[]{String.valueOf(walletId)});
        db.close();
    }

    private Cursor getWalletDataByUser(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_WALLET_NAME +
                " WHERE " + COLUMN_WALLET_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{userId});
        return cursor;
    }

    private Cursor getWalletDataById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_WALLET_NAME +
                " WHERE " + COLUMN_WALLET_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});
        return cursor;
    }

    private Wallet getWalletFromData(Cursor data) {
        int id = data.getInt(data.getColumnIndex(COLUMN_WALLET_ID));
        String name = data.getString(data.getColumnIndex(COLUMN_WALLET_NAME));
        float balance = data.getFloat(data.getColumnIndex(COLUMN_WALLET_BALANCE));
        String currency = data.getString(data.getColumnIndex(COLUMN_WALLET_CURRENCY));
        int type = data.getInt(data.getColumnIndex(COLUMN_WALLET_TYPE));
        String icon = data.getString(data.getColumnIndex(COLUMN_WALLET_ICON));
        String userId = data.getString(data.getColumnIndex(COLUMN_WALLET_USER_ID));

        return new Wallet(id, name, balance, currency, type, icon, userId);
    }

    //

    // Transaction Type DataAccess

    public boolean insertTransactionType(TransactionType transactionType) {
        if (transactionType == null) return false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TRANSACTION_TYPE_TYPE, transactionType.getType());
        values.put(COLUMN_TRANSACTION_TYPE_CATEGORY, transactionType.getCategory());
        values.put(COLUMN_TRANSACTION_TYPE_ICON, transactionType.getIcon());
        if (transactionType.getParentType() != null) {
            values.put(COLUMN_TRANSACTION_TYPE_PARENT_ID, transactionType.getParentType().getId());
        }
        int id = (int) db.insert(TABLE_TRANSACTION_TYPE_NAME, COLUMN_TRANSACTION_TYPE_PARENT_ID, values);
        db.close();
        transactionType.setId(id);
        return id != -1;
    }

    public void updateTransactionType(TransactionType transactionType) {
        if (transactionType == null) return;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TRANSACTION_TYPE_TYPE, transactionType.getType());
        values.put(COLUMN_TRANSACTION_TYPE_CATEGORY, transactionType.getCategory());
        values.put(COLUMN_TRANSACTION_TYPE_ICON, transactionType.getIcon());
        values.put(COLUMN_TRANSACTION_TYPE_PARENT_ID, transactionType.getParentType().getId());
        db.update(TABLE_TRANSACTION_TYPE_NAME, values, COLUMN_TRANSACTION_TYPE_ID + " = ?", new String[]{String.valueOf(transactionType.getId())});
        db.close();
    }

    public TransactionType getTransactionTypeById(int id) {
        Cursor data = getTransactionTypeDataById(id);
        if (data != null && data.getCount() > 0) {
            data.moveToFirst();
            TransactionType transactionType = getTransactionTypeFromData(data);
            return transactionType;
        } else {
            return null;
        }
    }

    public List<TransactionType> getAllTransactionType() {
        Cursor data = getAllTransactionTypeData();
        List<TransactionType> list_result = new ArrayList<>();
        if (data != null && data.getCount() > 0) {
            data.moveToFirst();
            do {
                TransactionType transactionType = getTransactionTypeFromData(data);
                list_result.add(transactionType);
            }
            while (data.moveToNext());
        }
        return list_result;
    }

    private TransactionType getTransactionTypeFromData(Cursor data) {
        int id = data.getInt(data.getColumnIndex(COLUMN_TRANSACTION_TYPE_ID));
        int type = data.getInt(data.getColumnIndex(COLUMN_TRANSACTION_TYPE_TYPE));
        String category = data.getString(data.getColumnIndex(COLUMN_TRANSACTION_TYPE_CATEGORY));
        String icon = data.getString(data.getColumnIndex(COLUMN_TRANSACTION_TYPE_ICON));
        TransactionType parent = null;
        if (!data.isNull(data.getColumnIndex(COLUMN_TRANSACTION_TYPE_PARENT_ID))) {
            int parentId = data.getInt(data.getColumnIndex(COLUMN_TRANSACTION_TYPE_PARENT_ID));
            parent = getTransactionTypeById(parentId);
        }
        return new TransactionType(id, type, category, icon, parent);
    }

    private Cursor getTransactionTypeDataById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TRANSACTION_TYPE_NAME +
                " WHERE " + COLUMN_TRANSACTION_TYPE_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});
        return cursor;
    }

    private Cursor getAllTransactionTypeData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TRANSACTION_TYPE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    // Transaction

    public boolean insertTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TRANSACTION_CURRENCY, transaction.getCurrencyCode());
        values.put(COLUMN_TRANSACTION_TRADING, transaction.getMoneyTrading());
        values.put(COLUMN_TRANSACTION_DATE, transaction.getTransactionDate().getTime());
        values.put(COLUMN_TRANSACTION_NOTE, transaction.getTransactionNote());
        //values.put(COLUMN_TRANSACTION_LOCATION,transaction.getLocation());
        values.put(COLUMN_TRANSACTION_TYPE_ID_FK, transaction.getTransactionType().getId());
        values.put(COLUMN_WALLET_ID_FK, transaction.getWallet().getWalletId());
        int id = (int) db.insert(TABLE_TRANSACTION_NAME, COLUMN_TRANSACTION_LOCATION, values);
        transaction.setTransactionId(id);
        db.close();
        return id != -1;
    }

    public boolean updateTransaction(Transaction transaction) {
        //    COLUMN_WALLET_ID_FK                   + " INTEGER NOT NULL," +

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TRANSACTION_CURRENCY, transaction.getCurrencyCode());
        values.put(COLUMN_TRANSACTION_TRADING, transaction.getMoneyTrading());
        values.put(COLUMN_TRANSACTION_DATE, transaction.getTransactionDate().getTime());
        values.put(COLUMN_TRANSACTION_NOTE, transaction.getTransactionNote());
        //values.put(COLUMN_TRANSACTION_LOCATION,transaction.getLocation());
        values.put(COLUMN_TRANSACTION_TYPE_ID_FK, transaction.getTransactionType().getId());
        values.put(COLUMN_WALLET_ID_FK, transaction.getWallet().getWalletId());
        db.update(TABLE_TRANSACTION_NAME,values,COLUMN_TRANSACTION_ID + " = ?" ,new String[]{String.valueOf(transaction.getTransactionId())});
        db.close();
        return true;
    }

    public Transaction getTransactionById(int transactionId) {
        Cursor data = getTransactionDataById(transactionId);
        if(data != null && data.getCount() > 0) {
            data.moveToFirst();
            Transaction transaction = getTransactionFromData(data);
            return transaction;
        }
        return null;
    }

    public List<Transaction> getAllTransactionByWalletId(int walletId) {
        Cursor data = getAllTransactionDataByWalletId(walletId);
        List<Transaction> list_result = new ArrayList<>();
        if (data != null && data.getCount() > 0) {
            data.moveToFirst();
            do {
                Transaction transaction = getTransactionFromData(data);
                list_result.add(transaction);
            }
            while (data.moveToNext());
        }
        return list_result;
    }

    private Cursor getAllTransactionDataByWalletId(int walletId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TRANSACTION_NAME +
                " WHERE " + COLUMN_WALLET_ID_FK + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(walletId)});
        return cursor;
    }

    public List<Transaction> getAllTransaction() {
        Cursor data = getAllTransactionData();
        List<Transaction> list_result = new ArrayList<>();
        if (data != null && data.getCount() > 0) {
            data.moveToFirst();
            do {
                Transaction transaction = getTransactionFromData(data);
                list_result.add(transaction);
            }
            while (data.moveToNext());
        }
        return list_result;
    }

    private Cursor getAllTransactionData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TRANSACTION_NAME;
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }

    private Transaction getTransactionFromData(Cursor data) {
        Transaction.TransactionBuilder builder = new Transaction.TransactionBuilder();
        builder
                .setTransactionId(data.getInt(data.getColumnIndex(COLUMN_TRANSACTION_ID)))
                .setMoneyTrading(data.getFloat(data.getColumnIndex(COLUMN_TRANSACTION_TRADING)))
                .setTransactionDate(new Date(data.getLong(data.getColumnIndex(COLUMN_TRANSACTION_DATE))))
                .setTransactionNote(data.getString(data.getColumnIndex(COLUMN_TRANSACTION_NOTE)))
                .setCurrencyCode(data.getString(data.getColumnIndex(COLUMN_TRANSACTION_CURRENCY)));
        //String location = data.getString(data.getColumnIndex(COLUMN_TRANSACTION_NOTE));
        int typeId = data.getInt(data.getColumnIndex(COLUMN_TRANSACTION_TYPE_ID_FK));
        int walletId = data.getInt(data.getColumnIndex(COLUMN_WALLET_ID_FK));
        TransactionType transactionType = this.getTransactionTypeById(typeId);
        Wallet wallet = this.getWalletById(walletId);
        builder
                .setTransactionType(transactionType)
                .setWallet(wallet);
        return builder.build();
    }

    private Cursor getTransactionDataById(int transactionId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TRANSACTION_NAME +
                " WHERE " + COLUMN_TRANSACTION_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(transactionId)});
        return cursor;
    }



}
