package com.minhtzy.moneytracker.utilities;

import android.content.Context;

import com.minhtzy.moneytracker.dataaccess.IWalletsDAO;
import com.minhtzy.moneytracker.dataaccess.WalletsDAOImpl;
import com.minhtzy.moneytracker.model.Wallet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class WalletsManager {
    private IWalletsDAO iWalletsDAO;
    private List<Wallet> wallets;
    private static final WalletsManager ourInstance = new WalletsManager();

    public static WalletsManager getInstance(Context context) {
        ourInstance.setContext(context);
        return ourInstance;
    }

    private void setContext(Context context) {
        if(context != null) {
            this.iWalletsDAO = new WalletsDAOImpl(context);
        }
    }

    private WalletsManager() {
        wallets = new ArrayList<>();
    }
    public Wallet getCurrentWallet() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null) {
            return iWalletsDAO.getAllWalletByUser(currentUser.getUid()).get(0);
        }
        return null;
    }

    public boolean updateWallet(Wallet wallet) {
        return  iWalletsDAO.updateWallet(wallet);
    }

    public void updateTimestamp(int walletId, long time) {
        iWalletsDAO.updateTimeStamp(walletId,time);

    }
}
