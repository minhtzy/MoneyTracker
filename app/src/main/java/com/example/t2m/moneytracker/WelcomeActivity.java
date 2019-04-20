package com.example.t2m.moneytracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.t2m.moneytracker.account.LoginActivity;
import com.example.t2m.moneytracker.wallet.AddWalletActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class WelcomeActivity extends AppCompatActivity {

    public static String DATABASE_NAME = "money_tracker.db";
    public static final String DB_PATH_SUFFIX = "/databases/";
    public static SQLiteDatabase database = null;
    public static final String DB_PREFS = "MoneyTrackerSharedPrefs";
    public static final String KEY_IS_FIRST_TIME_INIT_DATABASE = "moneytracker.db.sharedprefs.key.is_first_time";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
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
