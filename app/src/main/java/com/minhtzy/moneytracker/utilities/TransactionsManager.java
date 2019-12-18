package com.minhtzy.moneytracker.utilities;


import android.content.Context;

import com.minhtzy.moneytracker.dataaccess.IBudgetDAO;
import com.minhtzy.moneytracker.dataaccess.ITransactionsDAO;
import com.minhtzy.moneytracker.dataaccess.IWalletsDAO;
import com.minhtzy.moneytracker.dataaccess.TransactionsDAOImpl;
import com.minhtzy.moneytracker.entity.TransactionEntity;
import com.minhtzy.moneytracker.entity.WalletEntity;
import com.minhtzy.moneytracker.model.DateRange;
import com.minhtzy.moneytracker.model.TransactionTypes;
import com.minhtzy.moneytracker.sync.SyncCloudFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionsManager {

    private List<TransactionEntity> transactions;
    private ITransactionsDAO iTransactionsDAO;

    Context mContext;
    private static final TransactionsManager ourInstance = new TransactionsManager();

    public static TransactionsManager getInstance(Context context) {
        ourInstance.setContext(context);
        return ourInstance;
    }

    private TransactionsManager() {
        transactions = new ArrayList<>();
    }
    private void setContext(Context context) {
        if(context != null) {
            this.mContext = context;
            iTransactionsDAO = new TransactionsDAOImpl(context);
        }
    }

    public List<TransactionEntity> getAllTransactions() {
        return transactions;
    }

    public List<TransactionEntity> getAllTransactionByTime(DateRange dateRange) {
        List<TransactionEntity> filterTransaction = new ArrayList<>();

        com.minhtzy.moneytracker.utilities.DateUtils dateUtils = new com.minhtzy.moneytracker.utilities.DateUtils();
        for(TransactionEntity tran : transactions) {
            Date date = tran.getTransactionTime();
            if(dateUtils.isDateRangeContainDate(dateRange,date)) {
                filterTransaction.add(tran);
            }
        }
        return filterTransaction;

    }

    public boolean addTransaction(TransactionEntity transaction) {
        if(transaction == null) return  false;
        boolean inserted = iTransactionsDAO.insertTransaction(transaction);
        if(inserted)
        {
            WalletsManager.getInstance(mContext).notifyChanged(transaction.getWalletId());
            BudgetsManager.getInstance(mContext).notifyChanged();

        }

        return inserted;
    }
    public boolean updateTransaction(TransactionEntity transaction, TransactionEntity oldTransaction) {
        if(transaction == null) return  false;
        boolean updated = iTransactionsDAO.updateTransaction(transaction);
        if(updated)
        {
            WalletsManager.getInstance(mContext).notifyChanged(transaction.getWalletId());
            BudgetsManager.getInstance(mContext).notifyChanged();
        }
        return updated;
    }

    public boolean deleteTransaction(TransactionEntity transaction) {
        if(transaction == null) return  false;
        boolean deleted = iTransactionsDAO.deleteTransaction(transaction);
        if(deleted)
        {
            WalletsManager.getInstance(mContext).notifyChanged(transaction.getWalletId());
            BudgetsManager.getInstance(mContext).notifyChanged();
        }
        return deleted;
    }

    private void getAllTransactions(WalletEntity wallet) {
        transactions.clear();
        if(wallet != null){
            transactions = iTransactionsDAO.getAllTransactionByWalletId(wallet.getWalletId());
        }
    }
}
