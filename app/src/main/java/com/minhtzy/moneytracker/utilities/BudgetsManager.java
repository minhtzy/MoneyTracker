package com.minhtzy.moneytracker.utilities;

import android.content.Context;

import com.minhtzy.moneytracker.dataaccess.BudgetDAOImpl;
import com.minhtzy.moneytracker.dataaccess.IBudgetDAO;
import com.minhtzy.moneytracker.entity.BudgetEntity;
import com.minhtzy.moneytracker.notifications.BudgetNotifications;

import java.util.List;

class BudgetsManager {


    IBudgetDAO iBudgetDAO;

    public Context getContext() {
        return context;
    }

    Context context;

    List<BudgetEntity> budgets;

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
            budgets = iBudgetDAO.getAllBudget();
        }
    }


    public void notifyChanged() {
        budgets = iBudgetDAO.getAllBudget();
        for(BudgetEntity budget : budgets)
        {
            if(budget.getSpent() > budget.getBudgetAmount())
            {
                BudgetNotifications noti = new BudgetNotifications(context);
                noti.notifyBudgetOverSpending(budget);
            }
        }
    }

    public List<BudgetEntity> getAllBudgets()
    {
        return budgets;
    }
}
