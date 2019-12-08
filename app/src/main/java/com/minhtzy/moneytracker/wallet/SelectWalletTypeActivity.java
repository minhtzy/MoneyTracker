package com.minhtzy.moneytracker.wallet;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.utilities.WalletsManager;

public class SelectWalletTypeActivity extends AppCompatActivity {

    private TextView mBtnBasicWallet;
    private TextView mBtnGoalWallet;
    private TextView mBtnLinkerWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_wallet_type);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addControls();
        addEvents();
    }

    private void addControls() {
        mBtnBasicWallet = findViewById(R.id.btnBasicWallet);
        mBtnGoalWallet = findViewById(R.id.btnGoalWallet);
        mBtnLinkerWallet = findViewById(R.id.btnLinkedWallet);
    }

    private void addEvents() {
        mBtnBasicWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectWalletTypeActivity.this,CreateBasicWalletActivity.class);
                startActivity(intent);
            }
        });

        mBtnGoalWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mBtnLinkerWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        if(WalletsManager.getInstance(this).getAllWallet().isEmpty())
        {
            Toast.makeText(this,"Bạn cần phải có ví trong tài khoản. Tạo ngay!",Toast.LENGTH_LONG).show();
            return false;
        }
        finish();
        return true;
    }
}
