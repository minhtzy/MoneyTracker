package com.minhtzy.moneytracker.dataaccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.minhtzy.moneytracker.entity.WalletEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WalletsDAOImpl implements IWalletsDAO {

    /**
     * Wallet table
     * int walletId
     * String walletName;
     * float currentBalance;
     * CurrencyFormat currency;
     * int walletType;
     * String imageSrc;
     * String userUID;
     */

    public static final String TABLE_WALLET_NAME = "tbl_wallets";

    private MoneyTrackerDBHelper dbHelper;
    public WalletsDAOImpl(Context context) {
        dbHelper = new MoneyTrackerDBHelper(context);
    }

    // Wallet data access

    public WalletEntity getWalletById(String id) {
        Cursor data = getWalletDataById(id);
        if (data != null && data.getCount() > 0) {
            data.moveToFirst();
            WalletEntity wallet = new WalletEntity();
            wallet.loadFromCursor(data);
            return wallet;
        } else {
            return null;
        }
    }

    public boolean hasWallet(String userId) {
        Cursor data = getWalletDataByUser(userId);
        return data != null && data.getCount() > 0;
    }
    public List<WalletEntity> getAllWalletByUser(String userId) {
        Cursor data = getWalletDataByUser(userId);
        List<WalletEntity> list_wallet = new ArrayList<>();
        if (data != null && data.getCount() > 0) {
            data.moveToFirst();
            do {
                WalletEntity wallet = new WalletEntity();
                wallet.loadFromCursor(data);
                list_wallet.add(wallet);
            }
            while (data.moveToNext());
        }
        return list_wallet;
    }

    public boolean insertWallet(WalletEntity wallet) {
        if(wallet == null) return false;
        if (wallet.getWalletId().isEmpty())
        {
            wallet.setWalletId(UUID.randomUUID().toString());
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        wallet.setTimeStamp(com.google.firebase.Timestamp.now().toDate().getTime());
        long insertId = db.insert(TABLE_WALLET_NAME, null, wallet.getContentValues());
        db.close();
        return insertId != -1;
    }

    public boolean updateWallet(WalletEntity wallet) {
        if(wallet == null) return false;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        wallet.setTimeStamp(com.google.firebase.Timestamp.now().toDate().getTime());
        ContentValues values = wallet.getContentValues();
        int updated = db.update(TABLE_WALLET_NAME, values, WalletEntity.WALLET_ID + " = ?", new String[]{String.valueOf(wallet.getWalletId())});
        db.close();
        return updated > 0;
    }

    public boolean deleteWallet(String walletId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deleted = db.delete(TABLE_WALLET_NAME, WalletEntity.WALLET_ID + " = ?", new String[]{String.valueOf(walletId)});
        db.close();
        return deleted > 0;
    }

    private Cursor getWalletDataByUser(String userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_WALLET_NAME +
                " WHERE " + WalletEntity.USER_ID + " = ?";
        return db.rawQuery(query, new String[]{userId});
    }

    private Cursor getWalletDataById(String id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_WALLET_NAME +
                " WHERE " + WalletEntity.WALLET_ID + " = ?";
        return db.rawQuery(query, new String[]{String.valueOf(id)});
    }


    public List<WalletEntity> getAllWalletNeedSync(String userId,long timestamp) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_WALLET_NAME +
                " WHERE " + WalletEntity.USER_ID + " = ?" +
                " AND " + WalletEntity.TIME_STAMP + " > ?";
        Cursor data = db.rawQuery(query,new String[]{userId,String.valueOf(timestamp)});
        List<WalletEntity> list_wallet = new ArrayList<>();
        if (data != null && data.getCount() > 0) {
            data.moveToFirst();
            do {
                WalletEntity wallet = new WalletEntity();
                wallet.loadFromCursor(data);
                list_wallet.add(wallet);
            }
            while (data.moveToNext());
        }
        db.close();
        return list_wallet;
    }
}
