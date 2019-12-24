package com.minhtzy.moneytracker.sync;

public interface SyncEvents {
    void onPullCompleted();
    void onPullFailed();

}
