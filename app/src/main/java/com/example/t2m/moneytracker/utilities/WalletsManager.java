package com.example.t2m.moneytracker.utilities;

import com.example.t2m.moneytracker.dataaccess.MoneyTrackerDBHelper;
import com.example.t2m.moneytracker.model.Wallet;

import java.util.List;

public class WalletsManager {
    private MoneyTrackerDBHelper dbHelper;
    private List<Wallet> wallets;
    private static final WalletsManager ourInstance = new WalletsManager();

    public static WalletsManager getInstance() {
        return ourInstance;
    }

    private WalletsManager() {
        //dbHelper = new MoneyTrackerDBHelper(null);
    }
}
