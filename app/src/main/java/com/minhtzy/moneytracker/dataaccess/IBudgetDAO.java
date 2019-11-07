package com.minhtzy.moneytracker.dataaccess;

import com.minhtzy.moneytracker.model.Budget;
import com.minhtzy.moneytracker.model.DateRange;

import java.util.List;

public interface IBudgetDAO {
    public boolean insertBudget(Budget budget);
    public boolean updateBudget(Budget budget);
    public boolean deleteBudget(Budget budget);
    public List<Budget> getAllBudget();
    public List<Budget> getAllBudget(long walletId);
    public List<Budget> getBudgetByPeriod(long walletId,DateRange dateRange);
    public List<Budget> getBudgetByCategory(long walletId,int categoryId);
    public void updateBudgetSpent(Budget budget);
}
