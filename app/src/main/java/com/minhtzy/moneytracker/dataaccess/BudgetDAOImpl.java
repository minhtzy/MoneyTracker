package com.minhtzy.moneytracker.dataaccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.minhtzy.moneytracker.budget.BudgetFragment;
import com.minhtzy.moneytracker.entity.BudgetEntity;

import java.util.ArrayList;
import java.util.List;

public class BudgetDAOImpl implements IBudgetDAO {

    public static final String TABLE_BUDGET = "tbl_budgets";

    private MoneyTrackerDBHelper dbHelper;

    public BudgetDAOImpl(Context context) {
        dbHelper = new MoneyTrackerDBHelper(context);
    }

    @Override
    public boolean insertBudget(BudgetEntity budget) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = budget.getContentValues();
        values.remove(BudgetEntity.BUDGET_ID);
        int id = (int) db.insert(TABLE_BUDGET,BudgetEntity.BUDGET_ID,values);
        budget.setBudgetId(id);
        db.close();
        return id != -1;
    }

    @Override
    public boolean updateBudget(BudgetEntity budget) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = budget.getContentValues();
        int updated = db.update(TABLE_BUDGET,values,BudgetEntity.BUDGET_ID + " = ?",new String[]{String.valueOf(budget.getBudgetId())});
        db.close();
        return updated > 0;
    }

    @Override
    public boolean deleteBudget(BudgetEntity budget) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deleted = db.delete(TABLE_BUDGET, BudgetEntity.BUDGET_ID + " = ?", new String[]{String.valueOf(budget.getBudgetId())});
        db.close();
        return deleted > 0;
    }

    @Override
    public List<BudgetEntity> getAllBudget() {
        List<BudgetEntity> list_result = new ArrayList<>();
        Cursor data = getAllBudgetData();
        if(data != null && data.getCount() > 0) {
            data.moveToFirst();
            do {
                list_result.add(getBudgetFromData(data));
            }while (data.moveToNext());
        }
        return list_result;
    }

    @Override
    public List<BudgetEntity> getAllBudget(String walletId) {
        List<BudgetEntity> list_result = new ArrayList<>();
        Cursor data = getAllBudgetData(walletId);
        if(data != null && data.getCount() > 0) {
            data.moveToFirst();
            do {
                list_result.add(getBudgetFromData(data));
            }while (data.moveToNext());
        }
        return list_result;
    }


    private Cursor getAllBudgetData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_BUDGET;
        return db.rawQuery(query,new String[]{});
    }

    private Cursor getAllBudgetData(String walletId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_BUDGET +
                " WHERE " + BudgetEntity.WALLET_ID + " = ?";
        return db.rawQuery(query, new String[]{String.valueOf(walletId)});
    }

    private BudgetEntity getBudgetFromData(Cursor data) {
        BudgetEntity budget = new BudgetEntity();
        budget.loadFromCursor(data);
        updateBudgetSpent(budget);
        return budget;
    }

    public void updateBudgetSpent(BudgetEntity budget) {
        String walletId = budget.getWalletId();
        int categoryId = budget.getCategoryId();
        long timeStart = budget.getPeriod().getDateFrom().getMillis();
        long timeEnd = budget.getPeriod().getDateTo().getMillis();
        String query =
                        "SELECT SUM(amount)" +
                        " FROM tbl_transactions as t" +
                        " INNER JOIN tbl_categories as c" +
                        " ON t.categoryId = c._id" +
                        " WHERE t.walletId = \'" + walletId +
                        "\' AND time >= " + timeStart + " AND time <= " + timeEnd +
                        " AND (c._id = " + categoryId +" OR c.parentId = " + categoryId + ")" ;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        double spent = 0;
        if(cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            spent = cursor.getDouble(0);
        }
        budget.setSpent(Math.abs(spent));
        db.close();
    }

}
