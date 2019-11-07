package com.minhtzy.moneytracker.dataaccess;

import com.minhtzy.moneytracker.model.DateRange;
import com.minhtzy.moneytracker.model.Statistical;

import java.util.List;

public interface ITStatistical {

    public List<Statistical> getAllStatistical();
    public List<Statistical> getAllStatisticalByWalletId(int walletId);
    public List<Statistical> getAllStatisticalByPeriod(int walletId,DateRange dateRange);
}
