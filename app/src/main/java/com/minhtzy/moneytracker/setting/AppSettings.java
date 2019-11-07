package com.minhtzy.moneytracker.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppSettings extends SettingsBase {

    public AppSettings(Context context) {
        super(context);
    }

    @Override
    protected SharedPreferences getPreferences() {
        return  PreferenceManager.getDefaultSharedPreferences(getContext());
    }
}
