package com.example.t2m.moneytracker;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.t2m.moneytracker.account.Login;
import com.example.t2m.moneytracker.dataaccess.MoneyTrackerDBHelper;
import com.example.t2m.moneytracker.wallet.Addwallet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
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
            Intent intent = new Intent(this,Login.class);
            startActivity(intent);
        }
        // Kiểm tra nếu chưa có ví
        else if(false) {
            Intent intent = new Intent(this,Addwallet.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
