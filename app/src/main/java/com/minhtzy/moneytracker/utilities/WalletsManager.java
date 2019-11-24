package com.minhtzy.moneytracker.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.minhtzy.moneytracker.App;
import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.dataaccess.IWalletsDAO;
import com.minhtzy.moneytracker.dataaccess.WalletsDAOImpl;
import com.minhtzy.moneytracker.entity.WalletEntity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.minhtzy.moneytracker.model.Constants;
import com.minhtzy.moneytracker.model.Language;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class WalletsManager {
    private IWalletsDAO iWalletsDAO;
    private static final WalletsManager ourInstance = new WalletsManager();

    public static WalletsManager getInstance(Context context) {
        ourInstance.setContext(context);
        return ourInstance;
    }

    private void setContext(Context context) {
        if(context != null) {
            this.iWalletsDAO = new WalletsDAOImpl(context);
        }
    }

    private WalletsManager() {

    }
    public WalletEntity getCurrentWallet() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentId = SharedPrefs.getInstance().get(SharedPrefs.KEY_CURRENT_WALLET,"");
        if(currentUser != null)
        {
            if(currentId.isEmpty()) {
                List<WalletEntity> allWallets = iWalletsDAO.getAllWalletByUser(currentUser.getUid());
                if(!allWallets.isEmpty())
                {
                    switchWallet(allWallets.get(0).getWalletId());
                    return allWallets.get(0);
                }

            }
            else {
                return iWalletsDAO.getWalletById(currentId);
            }
        }

        return null;
    }

    public WalletEntity getWalletById(String walletId)
    {
        return iWalletsDAO.getWalletById(walletId);
    }

    public void switchWallet(String walletId)
    {
        String currentId = SharedPrefs.getInstance().get(SharedPrefs.KEY_CURRENT_WALLET,"");
        if(currentId.equals(walletId)) return;
        SharedPrefs.getInstance().put(SharedPrefs.KEY_CURRENT_WALLET,walletId);
    }

    public boolean updateWallet(WalletEntity wallet) {
        return  iWalletsDAO.updateWallet(wallet);
    }

    public void notifyChanged(String walletId) {
    }

    public boolean hasWallet(String uid) {
        return iWalletsDAO.hasWallet(uid);
    }

    public List<WalletEntity> getAllWallet() {
        return iWalletsDAO.getAllWalletByUser(FirebaseAuth.getInstance().getUid());
    }

    public static class LanguageUtils {

        private static Language sCurrentLanguage = null;

        public static Language getCurrentLanguage() {
            if (sCurrentLanguage == null) {
                sCurrentLanguage = initCurrentLanguage();
            }

            return sCurrentLanguage;
        }

        /**
         * check language exist in SharedPrefs, if not exist then default language is Vietnam
         */
        public static Language initCurrentLanguage() {
            Language currentLanguage =
                    SharedPrefs.getInstance().get(SharedPrefs.LANGUAGE, Language.class);
            if (currentLanguage != null) {
                return currentLanguage;
            }
            currentLanguage = new Language(Constants.Value.DEFAULT_LANGUAGE_ID,
                    App.self().getString(R.string.language_vietnamese),
                    App.self().getString(R.string.language_vietnamese_code));
            SharedPrefs.getInstance().put(SharedPrefs.LANGUAGE, currentLanguage);
            return currentLanguage;

        }

        /**
         * return language list from string.xml
         */
        public static List<Language> getLanguageData() {
            List<Language> languageList = new ArrayList<>();
            List<String> languageNames =
                    Arrays.asList(App.self().getResources().getStringArray(R.array.language_names));
            List<String> languageCodes =
                    Arrays.asList(App.self().getResources().getStringArray(R.array.language_codes));
            if (languageNames.size() != languageCodes.size()) {
                // error, make sure these arrays are same size
                return languageList;
            }
            for (int i = 0, size = languageNames.size(); i < size; i++) {
                languageList.add(new Language(i, languageNames.get(i), languageCodes.get(i)));
            }
            return languageList;
        }

        /**
         * load current locale and change language
         */
        public static void loadLocale() {
            changeLanguage(initCurrentLanguage());
        }

        /**
         * change app language
         */
        @SuppressWarnings("deprecation")
        public static void changeLanguage(Language language) {
            SharedPrefs.getInstance().put(SharedPrefs.LANGUAGE, language);
            sCurrentLanguage = language;
            Locale locale = new Locale(language.getCode());
            Resources resources = App.self().getResources();
            Configuration configuration = resources.getConfiguration();
            configuration.setLocale(locale);
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }
    }

    public static class SharedPrefs {

        private static final String PREFS_NAME = "MoneyTrackerSharedPrefs";
        public static final String LANGUAGE = "langauge";
        public static final String KEY_IS_FIRST_TIME = "moneytracker.sharedprefs.key.is_first_time";
        public static final String KEY_PUSH_TIME = "moneytracker.sharedprefs.key.push_time";
        public static final String KEY_PULL_TIME = "moneytracker.sharedprefs.key.pull_time";
        public static final String KEY_CURRENT_WALLET = "moneytracker.sharedprefs.key.current.wallet";
        private static SharedPrefs mInstance;
        private SharedPreferences mSharedPreferences;

        private SharedPrefs() {
            mSharedPreferences = App.self().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }

        public static SharedPrefs getInstance() {
            if (mInstance == null) {
                mInstance = new SharedPrefs();
            }

            return mInstance;
        }

        @SuppressWarnings("unchecked")
        public <T> T get(String key, Class<T> anonymousClass) {
            if (anonymousClass == String.class) {
                return (T) mSharedPreferences.getString(key, "");
            } else if (anonymousClass == Boolean.class) {
                return (T) Boolean.valueOf(mSharedPreferences.getBoolean(key, false));
            } else if (anonymousClass == Float.class) {
                return (T) Float.valueOf(mSharedPreferences.getFloat(key, 0));
            } else if (anonymousClass == Integer.class) {
                return (T) Integer.valueOf(mSharedPreferences.getInt(key, 0));
            } else if (anonymousClass == Long.class) {
                return (T) Long.valueOf(mSharedPreferences.getLong(key, 0));
            } else {
                return (T) App.self()
                        .getGSon()
                        .fromJson(mSharedPreferences.getString(key, ""), anonymousClass);
            }
        }

        @SuppressWarnings("unchecked")
        public <T> T get(String key, T defaultValue) {
            if (defaultValue == String.class) {
                return (T) mSharedPreferences.getString(key, (String) defaultValue);
            } else if (defaultValue == Boolean.class) {
                return (T) Boolean.valueOf(mSharedPreferences.getBoolean(key, (Boolean) defaultValue));
            } else if (defaultValue == Float.class) {
                return (T) Float.valueOf(mSharedPreferences.getFloat(key, (Float) defaultValue));
            } else if (defaultValue == Integer.class) {
                return (T) Integer.valueOf(mSharedPreferences.getInt(key, (Integer) defaultValue));
            } else if (defaultValue == Long.class) {
                return (T) Long.valueOf(mSharedPreferences.getLong(key, (Long) defaultValue));
            } else {
                return defaultValue;
            }
        }

        public <T> void put(String key, T data) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            if (data instanceof String) {
                editor.putString(key, (String) data);
            } else if (data instanceof Boolean) {
                editor.putBoolean(key, (Boolean) data);
            } else if (data instanceof Float) {
                editor.putFloat(key, (Float) data);
            } else if (data instanceof Integer) {
                editor.putInt(key, (Integer) data);
            } else if (data instanceof Long) {
                editor.putLong(key, (Long) data);
            } else {
                editor.putString(key, App.self().getGSon().toJson(data));
            }
            editor.apply();
        }

        public void clear() {
            mSharedPreferences.edit().clear().apply();
        }
    }
}
