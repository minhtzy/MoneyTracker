package com.minhtzy.moneytracker.dataaccess;

import com.minhtzy.moneytracker.entity.BudgetEntity;
import com.minhtzy.moneytracker.model.DateRange;

import java.util.List;

public interface IBudgetDAO {
    public boolean insertBudget(BudgetEntity budget);
    public boolean updateBudget(BudgetEntity budget);
    public boolean deleteBudget(BudgetEntity budget);
    public List<BudgetEntity> getAllBudget();
    public List<BudgetEntity> getAllBudget(long walletId);
    public void updateBudgetSpent(BudgetEntity budget);
}
