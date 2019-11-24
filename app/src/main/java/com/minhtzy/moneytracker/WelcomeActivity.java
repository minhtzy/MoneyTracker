package com.minhtzy.moneytracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.minhtzy.moneytracker.account.LoginActivity;
import com.minhtzy.moneytracker.utilities.WalletsManager;
import com.minhtzy.moneytracker.wallet.AddWalletActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // luu trang thai khi lan dau su dung ung dung,

        WalletsManager.SharedPrefs sharedPrefs = WalletsManager.SharedPrefs.getInstance();
        if (sharedPrefs.get(WalletsManager.SharedPrefs.KEY_IS_FIRST_TIME, true)){
            sharedPrefs.put(WalletsManager.SharedPrefs.KEY_IS_FIRST_TIME,false);
            WalletsManager.LanguageUtils.changeLanguage(WalletsManager.LanguageUtils.getCurrentLanguage());
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
        // Kiểm tra nếu chưa đăng nhập thì đăng nhập
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null) {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
        // Kiểm tra nếu chưa có ví
        else if(!WalletsManager.getInstance(this).hasWallet(user.getUid())) {
            Intent intent = new Intent(this, AddWalletActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        finish();
    }


}
