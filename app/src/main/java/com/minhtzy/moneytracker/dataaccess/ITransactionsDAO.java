package com.minhtzy.moneytracker.dataaccess;

import com.minhtzy.moneytracker.model.DateRange;
import com.minhtzy.moneytracker.model.Transaction;

import java.util.List;

public interface ITransactionsDAO {
    public boolean insertTransaction(Transaction transaction);
    public boolean updateTransaction(Transaction transaction);
    public boolean deleteTransaction(Transaction transaction);

    public Transaction getTransactionById(long transactionId);

    public List<Transaction> getAllTransaction();
    public List<Transaction> getAllTransactionByWalletId(long walletId);
    public List<Transaction> getAllTransactionByPeriod(long walletId,DateRange dateRange);

    public void updateTimeStamp(long transactionId, long timestamp);

    public List<Transaction> getAllSyncTransaction(long walletId,long timestamp);
}
