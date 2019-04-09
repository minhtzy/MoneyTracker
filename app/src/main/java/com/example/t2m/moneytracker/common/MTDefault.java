package com.example.t2m.moneytracker.common;

import com.example.t2m.moneytracker.model.Wallet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MTDefault {
    private Wallet mWallet;
    private static final MTDefault ourInstance = new MTDefault();

    public static MTDefault getInstance() {
        return ourInstance;
    }

    private MTDefault() {
    }

    public FirebaseUser getUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
    public Wallet getCurrentWallet() {
        return mWallet;
    }

    public void setCurrentWallet(Wallet wallet) {
        mWallet = wallet;
    }

}
