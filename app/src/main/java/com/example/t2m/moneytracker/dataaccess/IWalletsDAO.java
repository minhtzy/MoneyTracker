package com.example.t2m.moneytracker.dataaccess;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.t2m.moneytracker.model.Wallet;

import java.util.ArrayList;
import java.util.List;

public interface IWalletsDAO {

    public boolean insertWallet(Wallet wallet);
    public boolean updateWallet(Wallet wallet) ;
    public boolean deleteWallet(int walletId) ;
    public Wallet getWalletById(int id);
    public boolean hasWallet(String userId);
    public List<Wallet> getAllWalletByUser(String userId);

}
