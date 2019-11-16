package com.minhtzy.moneytracker.dataaccess;

import com.minhtzy.moneytracker.entity.CurrencyFormat;

import java.util.List;

public interface ICurrencyFormatDAO {

    List<CurrencyFormat> getAllCurrencyAvailable();
}
