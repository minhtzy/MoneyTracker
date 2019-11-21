package com.minhtzy.moneytracker.dataaccess;

import com.minhtzy.moneytracker.entity.TransactionEntity;
import com.minhtzy.moneytracker.model.DateRange;

import java.util.List;

public interface ITransactionsDAO {
    public boolean insertTransaction(TransactionEntity transaction);
    public boolean updateTransaction(TransactionEntity transaction);
    public boolean deleteTransaction(TransactionEntity transaction);

    public TransactionEntity getTransactionById(String transactionId);

    public List<TransactionEntity> getAllTransaction();
    public List<TransactionEntity> getAllTransactionByWalletId(String walletId);
    public List<TransactionEntity> getAllTransactionByPeriod(String walletId,DateRange dateRange);

    public List<TransactionEntity> getAllSyncTransaction(String walletId,long timestamp);
}
