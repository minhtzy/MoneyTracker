package com.minhtzy.moneytracker.common;

import android.content.Context;
import android.preference.Preference;

public class MTDefault {
    private Context mContext;
    private static final MTDefault ourInstance = new MTDefault();

    public static MTDefault getInstance(Context context) {
        ourInstance.mContext = context;
        return ourInstance;
    }

    Preference mPreference;
    private MTDefault() {
        //mPreference = getSharedPreferences(Constants.AppPreferences,Context.MODE_PRIVATE)
    }

    public String getStringForKey(String key,String defaultValue) {
        return defaultValue;
    }

}
