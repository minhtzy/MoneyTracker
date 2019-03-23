package com.example.t2m.moneytracker.utilities;

import com.example.t2m.moneytracker.model.Wallet;

import java.util.HashSet;
import java.util.Set;

public class WalletManager {
    private HashSet<Wallet> wallets;
    public WalletManager() {
        wallets = new HashSet<>();
    }

    public HashSet<Wallet> getAllWallet() {
        return wallets;
    }
}
