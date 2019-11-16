package com.minhtzy.moneytracker.utilities;

import android.text.TextUtils;

import com.minhtzy.moneytracker.entity.CurrencyFormat;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class CurrencyUtils {

    public static class CurrencySymbols {
        public static String NONE = "";
        public static String MALAYSIA = "RM";
        public static String INDONESIA = "Rp";
        public static String SRILANKA = "Rs";
        public static String USA = "$";
        public static String UK = "£";
        public static String INDIA = "₹";
        public static String PHILIPPINES = "₱";
        public static String PAKISTAN = "₨";
        public static String VIETNAMDONG = "đ";
    }


    /**
     * Get price with VietNam currency
     *
     * @param price
     */
    public static String formatVnCurrency(String price) {

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

    public static String formatCurrency(String price, CurrencyFormat currencyFormat) {
        String cleanString = price.replaceAll("[$,.]", "").replaceAll(currencyFormat.getCurrencySymbol(), "").replaceAll("\\s+", "");

        boolean spacing = false;
        boolean Decimals = !currencyFormat.getDecimalPoint().isEmpty();
        if (cleanString.length() != 0) {
            try {
                String currencyString = "";
                currencyString = currencyFormat.getGroupSeparator() + (spacing ? " " : "") + currencyFormat.getCurrencySymbol();

                double parsed;
                int parsedInt;
                String formatted;

                if (Decimals) {
                    parsed = Double.parseDouble(cleanString);
                    formatted = NumberFormat.getCurrencyInstance().format((parsed / 100)).replace(NumberFormat.getCurrencyInstance().getCurrency().getSymbol(), currencyString);
                } else {
                    parsedInt = Integer.parseInt(cleanString);
                    formatted = currencyFormat + NumberFormat.getNumberInstance(Locale.US).format(parsedInt);
                }

                return formatted;
            } catch (NumberFormatException e) {
                return "0" + currencyFormat.getCurrencySymbol();
            }
        }
        return "0" + currencyFormat.getCurrencySymbol();
    }
}
