package com.minhtzy.moneytracker.wallet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.Transaction;
import com.minhtzy.moneytracker.MainActivity;
import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.adapter.CurrencyFormatAdapter;
import com.minhtzy.moneytracker.dataaccess.CurrencyFormatDAOImpl;
import com.minhtzy.moneytracker.dataaccess.ICurrencyFormatDAO;
import com.minhtzy.moneytracker.dataaccess.IWalletsDAO;
import com.minhtzy.moneytracker.dataaccess.WalletsDAOImpl;
import com.minhtzy.moneytracker.entity.CategoryEntity;
import com.minhtzy.moneytracker.entity.CurrencyFormat;
import com.minhtzy.moneytracker.entity.TransactionEntity;
import com.minhtzy.moneytracker.entity.WalletEntity;
import com.minhtzy.moneytracker.entity.WalletType;
import com.minhtzy.moneytracker.model.Constants;
import com.minhtzy.moneytracker.model.TransactionTypes;
import com.minhtzy.moneytracker.utilities.TransactionsManager;
import com.minhtzy.moneytracker.utilities.WalletsManager;
import com.minhtzy.moneytracker.view.CurrencyEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;
import java.util.List;

public class AddWalletActivity extends AppCompatActivity {

    EditText txtTen;
    CurrencyEditText txtSotien;
    TextView textCurrency;
    List<CurrencyFormat> mListCurrencyFormat;
    CurrencyFormat mCurrencyFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wallet);
        // add back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addControls();
        addEvents();
    }

    private void addControls() {
        //
        txtTen = (EditText)findViewById(R.id.txtTen);
        txtSotien = (CurrencyEditText) findViewById(R.id.txtSotien);
        textCurrency = findViewById(R.id.text_currency_name);

        ICurrencyFormatDAO cfDao = new CurrencyFormatDAOImpl(this);
        mListCurrencyFormat = cfDao.getAllCurrencyAvailable();
    }

    private void addEvents() {
        textCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onClickCurrency();
            }
        });
    }

    private void onClickCurrency() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setIcon(R.drawable.ic_currency);
        builderSingle.setTitle("Chọn đơn vị tiền tệ");

        final ArrayAdapter arrayAdapter = new CurrencyFormatAdapter(this, R.layout.custom_item_currency,mListCurrencyFormat);

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCurrencyFormat = mListCurrencyFormat.get(which);
                updateUI();
                dialog.dismiss();
            }
        });
        builderSingle.show();
    }

    private void updateUI(){
        textCurrency.setText(mCurrencyFormat.getCurrencyName());
    }

    private void addWallet() {
        String name = txtTen.getText().toString();
        String sotien = txtSotien.getText().toString();
        if(name.isEmpty() ) {
            txtTen.setError("Tên không được để trống");
            txtTen.requestFocus();
            return;
        }
        if (sotien.isEmpty()){
            txtSotien.setError("Không được để trống");
            txtSotien.requestFocus();
            return;
        }

        initWallet();
    }

    private void initWallet() {
        int soTien = txtSotien.getCleanIntValue();
        String name = txtTen.getText().toString();
        IWalletsDAO iWalletsDAO = new WalletsDAOImpl(this);

        WalletEntity wallet = WalletEntity.create("",name,0, WalletType.BASIC_WALLET,"","VND",FirebaseAuth.getInstance().getUid());

        boolean added_wallet = iWalletsDAO.insertWallet(wallet);

        if(added_wallet) {
            TransactionEntity transaction = new TransactionEntity();
            transaction.setTransactionTime(new Date());
            transaction.setTransactionAmount(soTien);
            transaction.setTransactionNote("Initial Wallet");
            transaction.setCategoryId(56); // Other Income
            transaction.setWalletId(wallet.getWalletId());
            TransactionsManager.getInstance(this).addTransaction(transaction);

            WalletsManager.getInstance(this).switchWallet(wallet.getWalletId());
            Intent intent = new Intent(AddWalletActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(this,"Khởi tạo ví thất bại",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save : {
                addWallet();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onClickedBack();
        return true;
    }

    private void onClickedBack() {
        finish();
    }
}
