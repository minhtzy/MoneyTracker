package com.example.t2m.moneytracker.dataaccess;

import com.example.t2m.moneytracker.model.Budget;
import com.example.t2m.moneytracker.model.Category;
import com.example.t2m.moneytracker.model.DateRange;

import java.util.List;

public interface IBudgetDAO {
    public boolean insertBudget(Budget budget);
    public boolean updateBudget(Budget budget);
    public boolean deleteBudget(Budget budget);
    public List<Budget> getAllBudget();
    public List<Budget> getBudgetByPeriod(DateRange dateRange);
    public List<Budget> getBudgetByCategories(int id);

}
