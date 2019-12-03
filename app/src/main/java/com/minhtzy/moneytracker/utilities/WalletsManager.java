package com.minhtzy.moneytracker.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.minhtzy.moneytracker.App;
import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.dataaccess.IWalletsDAO;
import com.minhtzy.moneytracker.dataaccess.WalletsDAOImpl;
import com.minhtzy.moneytracker.entity.WalletEntity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.minhtzy.moneytracker.model.Constants;
import com.minhtzy.moneytracker.model.Language;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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
        String currentId = SharedPrefs.getInstance().get(SharedPrefs.KEY_CURRENT_WALLET,new String());
        if(currentUser != null)
        {
            if(currentId.isEmpty()) {
                List<WalletEntity> allWallets = iWalletsDAO.getAllWalletByUser(currentUser.getUid());
                if(!allWallets.isEmpty())
                {
                    switchWallet(allWallets.get(0).getWalletId());
                    return allWallets.get(0);
                }

            }
            else {
                return iWalletsDAO.getWalletById(currentId);
            }
        }

        return null;
    }

    public WalletEntity getWalletById(String walletId)
    {
        return iWalletsDAO.getWalletById(walletId);
    }

    public void switchWallet(String walletId)
    {
        String currentId = SharedPrefs.getInstance().get(SharedPrefs.KEY_CURRENT_WALLET,new String());
        if(currentId.equals(walletId)) return;
        SharedPrefs.getInstance().put(SharedPrefs.KEY_CURRENT_WALLET,walletId);
    }

    public boolean updateWallet(WalletEntity wallet) {
        return  iWalletsDAO.updateWallet(wallet);
    }

    public void notifyChanged(String walletId) {
    }

    public boolean hasWallet(String uid) {
        return iWalletsDAO.hasWallet(uid);
    }

    public List<WalletEntity> getAllWallet() {
        return iWalletsDAO.getAllWalletByUser(FirebaseAuth.getInstance().getUid());
    }

    public void deleteWallet(WalletEntity mWallet) {
        iWalletsDAO.deleteWallet(mWallet.getWalletId());
    }
}
