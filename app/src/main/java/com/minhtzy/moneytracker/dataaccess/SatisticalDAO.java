package com.minhtzy.moneytracker.dataaccess;

import android.content.Context;

import com.minhtzy.moneytracker.model.DateRange;
import com.minhtzy.moneytracker.model.Statistical;

import java.util.List;

public class SatisticalDAO implements ITStatistical{

    MoneyTrackerDBHelper dbHelper ;
    ICategoriesDAO iCategoriesDAO;

    public SatisticalDAO(Context context) {
        dbHelper = new MoneyTrackerDBHelper(context);
    }
    @Override
    public List<Statistical> getAllStatistical() {
        return null;
    }

    @Override
    public List<Statistical> getAllStatisticalByWalletId(int walletId) {
        return null;
    }

    @Override
    public List<Statistical> getAllStatisticalByPeriod(int walletId, DateRange dateRange) {
        return null;
    }



}
