package com.example.t2m.moneytracker.utilities;

import android.content.Context;
import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class CurrencyUtils {

  /**
   * Get price with VietNam currency
   *
   * @param price
   * */
  public static String formatVnCurrence(String price) {

    NumberFormat format = new DecimalFormat("#,##0.00");
    format.setCurrency(Currency.getInstance(Locale.US));

    price = (!TextUtils.isEmpty(price)) ? price : "0";
    price = price.trim();
    price = format.format(Double.parseDouble(price));
    price = price.replaceAll(",", "\\.");

    if (price.endsWith(".00")) {
      int centsIndex = price.lastIndexOf(".00");
      if (centsIndex != -1) {
        price = price.substring(0, centsIndex);
      }
    }
    price = String.format("%s đ", price);
    return price;
  }
}
