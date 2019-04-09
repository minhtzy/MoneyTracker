package com.example.t2m.moneytracker.utilities;

import android.content.Context;

import com.example.t2m.moneytracker.dataaccess.MoneyTrackerDBHelper;
import com.example.t2m.moneytracker.model.Wallet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class WalletsManager {
    private MoneyTrackerDBHelper dbHelper;
    private List<Wallet> wallets;
    private static final WalletsManager ourInstance = new WalletsManager();

    public static WalletsManager getInstance(Context context) {
        ourInstance.setContext(context);
        return ourInstance;
    }

    private void setContext(Context context) {
        if(context != null) {
            this.dbHelper = new MoneyTrackerDBHelper(context);
        }
    }

    private WalletsManager() {
        wallets = new ArrayList<>();
    }
    public Wallet getCurrentWallet() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null) {
            return dbHelper.getAllWalletByUser(currentUser.getUid()).get(0);
        }
        return null;
    }
}
