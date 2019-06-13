package com.example.t2m.moneytracker.utilities;


import android.content.Context;

import com.example.t2m.moneytracker.dataaccess.BudgetDAOImpl;
import com.example.t2m.moneytracker.dataaccess.CategoriesDAOImpl;
import com.example.t2m.moneytracker.dataaccess.IBudgetDAO;
import com.example.t2m.moneytracker.dataaccess.ICategoriesDAO;
import com.example.t2m.moneytracker.dataaccess.ITransactionsDAO;
import com.example.t2m.moneytracker.dataaccess.IWalletsDAO;
import com.example.t2m.moneytracker.dataaccess.MoneyTrackerDBHelper;
import com.example.t2m.moneytracker.dataaccess.TransactionsDAOImpl;
import com.example.t2m.moneytracker.dataaccess.WalletsDAOImpl;
import com.example.t2m.moneytracker.model.Budget;
import com.example.t2m.moneytracker.model.Category;
import com.example.t2m.moneytracker.model.DateRange;
import com.example.t2m.moneytracker.model.Transaction;
import com.example.t2m.moneytracker.model.TransactionTypes;
import com.example.t2m.moneytracker.model.Wallet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionsManager {

    private Wallet mWallet;
    private List<Transaction> transactions;
    private ITransactionsDAO iTransactionsDAO;
    private IWalletsDAO iWalletsDAO;
    private IBudgetDAO iBudgetDAO;

    Context mContext;
    private static final TransactionsManager ourInstance = new TransactionsManager();

    public static TransactionsManager getInstance(Context context) {
        ourInstance.setContext(context);
        return ourInstance;
    }

    private TransactionsManager() {
        mWallet = null;
        transactions = new ArrayList<>();
    }
    private void setContext(Context context) {
        if(context != null) {
            this.mContext = context;
            iTransactionsDAO = new TransactionsDAOImpl(context);
        }
    }

    public void setCurrentWallet(Wallet wallet) {
        this.mWallet = wallet;
        updateTransactions(wallet);
    }

    public List<Transaction> getAllTransactions() {
        return transactions;
    }

    public List<Transaction> getAllTransactionByTime(DateRange dateRange) {
        List<Transaction> filterTransaction = new ArrayList<>();

        DateUtils dateUtils = new DateUtils();
        for(Transaction tran : transactions) {
            Date date = tran.getTransactionDate();
            if(dateUtils.isDateRangeContainDate(dateRange,date)) {
                filterTransaction.add(tran);
            }
        }
        return filterTransaction;

    }

    public boolean addTransaction(Transaction transaction) {
        transactions.add(transaction);
        iTransactionsDAO.insertTransaction(transaction);
        WalletsManager.getInstance(mContext).updateWallet(transaction);
        if(transaction.getCategory().getType() == TransactionTypes.EXPENSE || transaction.getCategory().getType() == TransactionTypes.DEBIT)
        if(transaction.getMoneyTradingWithSign() < 0)
            BudgetsManager.getInstance(mContext).updateBudget(transaction);
        return true;
    }
    private boolean updateTransaction(Transaction transaction) {
        return true;
    }

    private boolean deleteTransaction(Transaction transaction) {
        return true;
    }

    private void updateTransactions(Wallet wallet) {
        transactions.clear();
        if(wallet != null){
            transactions = iTransactionsDAO.getAllTransactionByWalletId(wallet.getWalletId());
        }
    }
}
