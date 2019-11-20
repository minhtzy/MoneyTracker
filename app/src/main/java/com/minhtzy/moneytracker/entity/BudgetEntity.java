package com.minhtzy.moneytracker.entity;

import android.content.ContentValues;

import com.minhtzy.moneytracker.model.Constants;
import com.minhtzy.moneytracker.model.DateRange;
import com.minhtzy.moneytracker.model.MTDate;

import java.io.Serializable;

public class BudgetEntity extends EntityBase implements Serializable {

    public static final String BUDGET_ID = "_id";
    public static final String BUDGET_NAME = "name";
    public static final String CATEGORY_ID = "categoryId";
    public static final String BUDGET_AMOUNT = "amount";
    public static final String BUDGET_SPENT = "spent";
    public static final String WALLET_ID = "walletId";
    public static final String START_DATE = "timeStart";
    public static final String END_DATE = "timeEnd";
    public static final String STATUS = "status";

    public BudgetEntity() {
        super();
        setBudgetId(Constants.NOT_SET);
        setCategoryId(Constants.NOT_SET);
        setWalletId(Constants.NOT_SET);
    }

    public BudgetEntity(ContentValues contentValues) {
        super(contentValues);
    }

    public static BudgetEntity create()
    {
        return create(Constants.NOT_SET,"",0,Constants.NOT_SET,Constants.NOT_SET,new DateRange(new MTDate(),new MTDate()));
    }

    public static BudgetEntity create(int budgetId,String name,double amount,int categoryId,int walletId,DateRange period)
    {
        BudgetEntity budget = new BudgetEntity();
        budget.setBudgetId(budgetId);
        budget.setBudgetName(name);
        budget.setBudgetAmount(amount);
        budget.setCategoryId(categoryId);
        budget.setWalletId(walletId);
        budget.setPeriod(period);
        return budget;
    }

    public int getBudgetId() {
        return getInt(BUDGET_ID);
    }

    public String getBudgetName() {
        return getString(BUDGET_NAME);
    }

    public int getCategoryId() {
        return getInt(CATEGORY_ID);
    }

    public double getBudgetAmount() {
        return getDouble(BUDGET_AMOUNT);
    }

    public double getSpent() {
        return getDouble(BUDGET_SPENT);
    }

    public long getWalletId() {
        return getLong(WALLET_ID);
    }

    public DateRange getPeriod() {
        long startDate = getLong(START_DATE);
        long endDate = getLong(END_DATE);

        return new DateRange(new MTDate(startDate), new MTDate(endDate));
    }

    public MTDate getTimeStart()
    {
        return new MTDate(getLong(START_DATE));
    }

    public MTDate getTimeEnd()
    {
        return new MTDate(getLong(END_DATE));
    }

    public  String getStatus()
    {
        return getString(STATUS);
    }

    public void setBudgetId(int budget_id) {
        setInt(BUDGET_ID,budget_id);
    }

    public void setBudgetName(String name) {
        setString(BUDGET_NAME,name);
    }

    public void setCategoryId(int category_id) {
        setInt(CATEGORY_ID,category_id);
    }

    public void setBudgetAmount(double amount) {
        setDouble(BUDGET_AMOUNT,amount);
    }

    public void setWalletId(long walletId) {
        setLong(WALLET_ID,walletId);
    }

    public void setPeriod(DateRange period) {
        setLong(START_DATE,period.getDateFrom().getMillis());
        setLong(END_DATE,period.getDateTo().getMillis());
    }

    public void setStatus(String status)
    {
        setString(STATUS,status);
    }
}
