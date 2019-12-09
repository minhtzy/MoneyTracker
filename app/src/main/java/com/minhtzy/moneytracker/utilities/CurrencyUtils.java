package com.minhtzy.moneytracker.utilities;

import android.text.TextUtils;
import android.util.Log;

import com.minhtzy.moneytracker.App;
import com.minhtzy.moneytracker.dataaccess.CurrencyFormatDAOImpl;
import com.minhtzy.moneytracker.dataaccess.ICurrencyFormatDAO;
import com.minhtzy.moneytracker.entity.CurrencyFormat;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class CurrencyUtils {

    private static final CurrencyUtils ourInstance = new CurrencyUtils();


    List<CurrencyFormat> availableCurrencies;

    public static CurrencyUtils getInstance()
    {
        return ourInstance;
    }

    private CurrencyUtils()
    {
        ICurrencyFormatDAO icfDAO = new CurrencyFormatDAOImpl(App.self());
        availableCurrencies = icfDAO.getAllCurrencyAvailable();
    }

    public static String formatCurrency(String price, String currencyCode) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setCurrency(Currency.getInstance(currencyCode));
        format.setMaximumFractionDigits(0);
        return format.format(Double.valueOf(price));
    }

    public CurrencyFormat getCurrrencyFormat(String currencyCode)
    {
        for(CurrencyFormat currency : availableCurrencies)
        {
            if(currency.getCurrencyCode().compareTo(currencyCode) == 0)
            {
                return currency;
            }
        }
        return null;
    }

    public static double getCleanDouble(String price,String currencyCode) {
        //price = price.replaceAll("-",""); // remove negative symbol
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setCurrency(Currency.getInstance(currencyCode));
        format.setMaximumFractionDigits(0);
        try {
            return format.parse(price).doubleValue();
        } catch (ParseException e) {
            try {
                Log.d("Currency Utils","Parse currency failed");
                return Double.parseDouble(price);
            } catch (NumberFormatException ex) {
                Log.d("Currency Utils","Parse double failed");
            }
        }
        return 0.0;
    }

    public List<CurrencyFormat> getAllAvailableCurrency() {
        return availableCurrencies;
    }
}
