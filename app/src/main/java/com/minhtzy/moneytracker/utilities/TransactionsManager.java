package com.minhtzy.moneytracker.utilities;


import android.content.Context;

import com.minhtzy.moneytracker.dataaccess.IBudgetDAO;
import com.minhtzy.moneytracker.dataaccess.ITransactionsDAO;
import com.minhtzy.moneytracker.dataaccess.IWalletsDAO;
import com.minhtzy.moneytracker.dataaccess.TransactionsDAOImpl;
import com.minhtzy.moneytracker.model.DateRange;
import com.minhtzy.moneytracker.model.TransactionTypes;

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
        getAllTransactions(wallet);
    }

    public List<Transaction> getAllTransactions() {
        return transactions;
    }

    public List<Transaction> getAllTransactionByTime(DateRange dateRange) {
        List<Transaction> filterTransaction = new ArrayList<>();

        com.minhtzy.moneytracker.utilities.DateUtils dateUtils = new com.minhtzy.moneytracker.utilities.DateUtils();
        for(Transaction tran : transactions) {
            Date date = tran.getTransactionDate();
            if(dateUtils.isDateRangeContainDate(dateRange,date)) {
                filterTransaction.add(tran);
            }
        }
        return filterTransaction;

    }

    public boolean addTransaction(Transaction transaction) {
        if(transaction == null) return  false;
        Transaction hasTran = iTransactionsDAO.getTransactionById(transaction.getTransactionId());
        if(hasTran != null) {
            return false;
        }
        transactions.add(transaction);
        iTransactionsDAO.insertTransaction(transaction);

        Transaction tranInfo = iTransactionsDAO.getTransactionById(transaction.getTransactionId());
        if(tranInfo == null) return false;
        // update wallet
        tranInfo.getWallet().setCurrentBalance(tranInfo.getWallet().getCurrentBalance() + tranInfo.getMoneyTradingWithSign());
        com.minhtzy.moneytracker.utilities.WalletsManager.getInstance(mContext).updateWallet(tranInfo.getWallet());
        if(tranInfo.getCategory().getType() == TransactionTypes.EXPENSE || tranInfo.getCategory().getType() == TransactionTypes.DEBIT) {
            com.minhtzy.moneytracker.utilities.BudgetsManager.getInstance(mContext).updateBudget(tranInfo, 1,true);
        }
        return true;
    }
    public boolean updateTransaction(Transaction transaction, Transaction oldTransaction) {
        if(transaction == null) return  false;
        iTransactionsDAO.updateTransaction(transaction);

        float walletBalance = transaction.getWallet().getCurrentBalance() - oldTransaction.getMoneyTradingWithSign() + transaction.getMoneyTradingWithSign();
        transaction.getWallet().setCurrentBalance(walletBalance);
        com.minhtzy.moneytracker.utilities.WalletsManager.getInstance(mContext).updateWallet(transaction.getWallet());
        if(oldTransaction.getCategory().getType() == TransactionTypes.EXPENSE || oldTransaction.getCategory().getType() == TransactionTypes.DEBIT) {
            com.minhtzy.moneytracker.utilities.BudgetsManager.getInstance(mContext).updateBudget(oldTransaction, -1,false);
        }
        if(transaction.getCategory().getType() == TransactionTypes.EXPENSE || transaction.getCategory().getType() == TransactionTypes.DEBIT) {
            com.minhtzy.moneytracker.utilities.BudgetsManager.getInstance(mContext).updateBudget(transaction, 1,true);
        }
        return true;
    }

    public boolean deleteTransaction(Transaction transaction) {
        if(transaction == null) return  false;
        iTransactionsDAO.deleteTransaction(transaction);
        transaction.getWallet().setCurrentBalance(transaction.getWallet().getCurrentBalance() - transaction.getMoneyTradingWithSign());
        com.minhtzy.moneytracker.utilities.WalletsManager.getInstance(mContext).updateWallet(transaction.getWallet());
        if(transaction.getCategory().getType() == TransactionTypes.EXPENSE || transaction.getCategory().getType() == TransactionTypes.DEBIT) {
            com.minhtzy.moneytracker.utilities.BudgetsManager.getInstance(mContext).updateBudget(transaction, -1,false);
        }
        return true;
    }

    private void getAllTransactions(Wallet wallet) {
        transactions.clear();
        if(wallet != null){
            transactions = iTransactionsDAO.getAllTransactionByWalletId(wallet.getWalletId());
        }
    }

    public void updateTimestamp(int transactionId, long timestamp) {
        iTransactionsDAO.updateTimeStamp(transactionId,timestamp);
    }
}
