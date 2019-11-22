package com.minhtzy.moneytracker.common;

public class Constants {
   public static final String AppPreferences;
   public static final String SettingsPreferences;

    // Date/Time
    public static final String ISO_DATE_FORMAT;
    public static final String ISO_DATE_SHORT_TIME_FORMAT;
    //public static final String ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
    public static final String ISO_8601_FORMAT;
    public static final String LONG_DATE_PATTERN;
    public static final String LONG_DATE_MEDIUM_DAY_PATTERN;

    // Database
    public static final String DEFAULT_DB_FILENAME;
  // Amount formats
   public static final String PRICE_FORMAT;
   static {
    AppPreferences = "MoneyTracker.App.SharedPreferences";
    SettingsPreferences = "MoneyTracker.Settings.SharedPreferences";
    ISO_DATE_FORMAT = "yyyy-MM-dd";
    ISO_DATE_SHORT_TIME_FORMAT = "yyyy-MM-dd HH:mm";
    ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    LONG_DATE_PATTERN = "EEEE, dd MMMM yyyy";
    LONG_DATE_MEDIUM_DAY_PATTERN = "EEE, dd MMMM yyyy";
    DEFAULT_DB_FILENAME = "money_tracker.sqlite";
    PRICE_FORMAT = "%.2f";
   }
}
