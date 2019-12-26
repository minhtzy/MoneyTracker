package com.minhtzy.moneytracker.view.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.minhtzy.moneytracker.dataaccess.TransactionsDAOImpl;

public class NetworkMonitor extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(checkNetworkConnection(context)) {
            TransactionsDAOImpl transactionsDAO = new TransactionsDAOImpl(context);
            transactionsDAO.getAllTransaction();
        }
    }

    public boolean checkNetworkConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return  (networkInfo != null && networkInfo.isConnected());
    }
}
