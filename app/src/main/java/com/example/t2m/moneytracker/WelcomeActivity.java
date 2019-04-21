package com.example.t2m.moneytracker;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.t2m.moneytracker.account.LoginActivity;
import com.example.t2m.moneytracker.changelanguage.ChangeLanguageActivity;
import com.example.t2m.moneytracker.changelanguage.LanguageAdapter;
import com.example.t2m.moneytracker.common.Constants;
import com.example.t2m.moneytracker.model.Language;
import com.example.t2m.moneytracker.utils.LanguageHelper;
import com.example.t2m.moneytracker.utils.LanguageUtils;
import com.example.t2m.moneytracker.utils.SharedPrefs;
import com.example.t2m.moneytracker.wallet.AddWalletActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;


import static com.example.t2m.moneytracker.utils.LanguageUtils.initCurrentLanguage;

public class WelcomeActivity extends AppCompatActivity {



    public Locale LANGUAGE_CURRENT;
    public String LanguageCurrent ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // luu trang thai khi lan dau su dung ung dung,

        SharedPrefs sharedPrefs = SharedPrefs.getInstance();
        if (sharedPrefs.get(SharedPrefs.KEY_IS_FIRST_TIME, true)){
            sharedPrefs.put(SharedPrefs.KEY_IS_FIRST_TIME,false);
            LanguageUtils.changeLanguage(LanguageUtils.getCurrentLanguage());
        }
        setupApplication();

    }



    private void setupApplication() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startApplication();
            }
        }, 1000);
    }
    private void startApplication() {
//        // Kiểm tra nếu chưa đăng nhập thì đăng nhập
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if(user == null) {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
//        }
//        // Kiểm tra nếu chưa có ví
//        else if(false) {
//            Intent intent = new Intent(this,AddWalletActivity.class);
//            startActivity(intent);
//        }
//        else {
//            Intent intent = new Intent(this,MainActivity.class);
//            startActivity(intent);
//        }
        finish();
    }


}
