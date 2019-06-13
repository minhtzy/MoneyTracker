package com.example.t2m.moneytracker.utilities;

import android.content.Context;

import com.example.t2m.moneytracker.dataaccess.BudgetDAOImpl;
import com.example.t2m.moneytracker.dataaccess.IBudgetDAO;
import com.example.t2m.moneytracker.model.Budget;
import com.example.t2m.moneytracker.model.Transaction;
import com.example.t2m.moneytracker.notifications.BudgetNotifications;

import java.util.List;

class BudgetsManager {


    IBudgetDAO iBudgetDAO;

    public Context getContext() {
        return context;
    }

    Context context;

    private BudgetsManager() {
    }

    private static final BudgetsManager ourInstance = new BudgetsManager();

    public static BudgetsManager getInstance(Context context) {
        ourInstance.setContext(context);
        return ourInstance;
    }

    private void setContext(Context context) {
        if(context != null) {
            this.context = context;
            iBudgetDAO = new BudgetDAOImpl(context);
        }
    }

    public void updateBudget(Transaction transaction) {
        // update budget
        List<Budget> budgets = iBudgetDAO.getBudgetByCategory(transaction.getWallet().getWalletId(),transaction.getCategory().getId());
        for(Budget budget : budgets) {
            iBudgetDAO.updateBudgetSpent(budget);
            if(budget.getSpent() > budget.getAmount()) {
                new BudgetNotifications(getContext()).notifyBudgetOverSpending(budget);
            }
        }

        // update parent's budget
        if(transaction.getCategory().getParentCategory() != null) {
            List<Budget> parentBudgets = iBudgetDAO.getBudgetByCategory(transaction.getWallet().getWalletId(),transaction.getCategory().getParentCategory().getId());
            for(Budget budget : parentBudgets) {
                iBudgetDAO.updateBudgetSpent(budget);
                new BudgetNotifications(getContext()).notifyBudgetOverSpending(budget);
            }
        }
    }
}
