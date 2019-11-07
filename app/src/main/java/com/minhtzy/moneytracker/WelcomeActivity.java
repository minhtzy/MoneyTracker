package com.minhtzy.moneytracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.minhtzy.moneytracker.account.LoginActivity;
import com.minhtzy.moneytracker.utils.LanguageUtils;
import com.minhtzy.moneytracker.utils.SharedPrefs;

public class WelcomeActivity extends AppCompatActivity {

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
