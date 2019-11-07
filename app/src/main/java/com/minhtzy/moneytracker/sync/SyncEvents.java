package com.minhtzy.moneytracker.sync;

public interface SyncEvents {
    void onPullWalletComplete();
    void onPullWalletFailure();
    void onPullTransactionComplete();
    void onPullTransactionFailure();

}
