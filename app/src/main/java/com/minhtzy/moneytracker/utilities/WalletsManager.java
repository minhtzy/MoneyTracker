package com.minhtzy.moneytracker.utilities;

import android.content.Context;

import com.minhtzy.moneytracker.dataaccess.IWalletsDAO;
import com.minhtzy.moneytracker.dataaccess.WalletsDAOImpl;
import com.minhtzy.moneytracker.entity.WalletEntity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.minhtzy.moneytracker.utils.SharedPrefs;

import java.util.ArrayList;
import java.util.List;

public class WalletsManager {
    private IWalletsDAO iWalletsDAO;
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

    }
    public WalletEntity getCurrentWallet() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentId = SharedPrefs.getInstance().get(SharedPrefs.KEY_CURRENT_WALLET,"");
        if(currentUser != null && !currentId.isEmpty()) {
            return iWalletsDAO.getWalletById(currentId);
        }
        return null;
    }

    public WalletEntity getWalletById(String walletId)
    {
        return iWalletsDAO.getWalletById(walletId);
    }

    public void switchWallet(String walletId)
    {
        String currentId = SharedPrefs.getInstance().get(SharedPrefs.KEY_CURRENT_WALLET,"");
        if(currentId == walletId) return;
        SharedPrefs.getInstance().put(SharedPrefs.KEY_CURRENT_WALLET,walletId);
    }

    public boolean updateWallet(WalletEntity wallet) {
        return  iWalletsDAO.updateWallet(wallet);
    }

    public void notifyChanged(String walletId) {
    }
}
