package com.example.t2m.moneytracker.utilities;


import android.content.Context;

import com.example.t2m.moneytracker.dataaccess.MoneyTrackerDBHelper;
import com.example.t2m.moneytracker.model.Transaction;
import com.example.t2m.moneytracker.model.Wallet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionsManager {

    private Wallet wallet;
    List<Transaction> transactions;
    MoneyTrackerDBHelper dbHelper = null;
    private static final TransactionsManager ourInstance = new TransactionsManager();

    public static TransactionsManager getInstance(Context context) {
        ourInstance.setContext(context);
        return ourInstance;
    }

    private TransactionsManager() {
        wallet = null;
        transactions = new ArrayList<>();
    }
    private void setContext(Context context) {
        if(context != null) dbHelper = new MoneyTrackerDBHelper(context.getApplicationContext());
    }

    public void setCurrentWallet(Wallet wallet) {
        this.wallet = wallet;
        updateTransactions(wallet);
    }

    public List<Transaction> getAllTransactions() {
        return transactions;
    }

    public List<Transaction> getAllTransactionByTime(int dayOfMonth,int month,int year) {
        List<Transaction> filterTransaction = new ArrayList<>();

        for(Transaction tran : transactions) {
            Date date = tran.getTransactionDate();
            int t_dayOfMonth = DateUtils.getDayOfMonth(date);
            int t_month = DateUtils.getMonth(date);
            int t_year = DateUtils.getYear(date);
            if((dayOfMonth == 0 || dayOfMonth == t_dayOfMonth) && (month == 0 || month == t_month) && year == t_year) {
                filterTransaction.add(tran);
            }
        }
        return filterTransaction;

    }

    public boolean addTransaction(Transaction transaction) {
        transactions.add(transaction);
        dbHelper.insertTransaction(transaction);
        updateWallet(transaction);
        return true;
    }

    private boolean updateWallet(Transaction transaction) {
        float balance = wallet.getCurrentBalance();
        balance -= transaction.getMoneyTrading();
        wallet.setCurrentBalance(balance);
        dbHelper.updateWallet(wallet);
        return true;
    }
    private void updateTransactions(Wallet wallet) {
        transactions.clear();
        if(wallet != null){
            transactions = dbHelper.getAllTransactionByWalletId(wallet.getWalletId());
        }
    }
}
