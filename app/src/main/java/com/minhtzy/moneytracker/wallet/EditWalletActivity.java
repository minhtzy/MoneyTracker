package com.minhtzy.moneytracker.wallet;

import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.view.adapter.CurrencyFormatAdapter;
import com.minhtzy.moneytracker.dataaccess.CurrencyFormatDAOImpl;
import com.minhtzy.moneytracker.dataaccess.ICurrencyFormatDAO;
import com.minhtzy.moneytracker.entity.CurrencyFormat;
import com.minhtzy.moneytracker.entity.TransactionEntity;
import com.minhtzy.moneytracker.entity.WalletEntity;
import com.minhtzy.moneytracker.utilities.CurrencyUtils;
import com.minhtzy.moneytracker.utilities.ResourceUtils;
import com.minhtzy.moneytracker.utilities.TransactionsManager;
import com.minhtzy.moneytracker.utilities.WalletsManager;
import com.minhtzy.moneytracker.view.CurrencyEditText;
import com.minhtzy.moneytracker.view.SelectIconActivity;

import org.parceler.Parcels;

import java.util.Date;
import java.util.List;

public class EditWalletActivity extends AppCompatActivity {

    public static final String EXTRA_WALLET = "com.minhtzy.moneytracker.extra.wallet";
    private static final int RC_ICON_PATH = 10 ;
    EditText txtTen;
    CurrencyEditText mTextAmount;
    TextView textCurrency;
    ImageView mWalletIcon;
    List<CurrencyFormat> mListCurrencyFormat;
    CurrencyFormat mCurrencyFormat;
    String mIconPath;

    WalletEntity mWallet;
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
        mTextAmount = (CurrencyEditText) findViewById(R.id.txtSotien);
        textCurrency = findViewById(R.id.text_currency_name);
        mWalletIcon = findViewById(R.id.wallet_icon);

        mWallet = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_WALLET));
        ICurrencyFormatDAO cfDao = new CurrencyFormatDAOImpl(this);
        mListCurrencyFormat = cfDao.getAllCurrencyAvailable();
        mIconPath = mWallet.getIcon();
        mCurrencyFormat = CurrencyUtils.getInstance().getCurrrencyFormat(mWallet.getCurrencyCode());
        loadInfo();
    }

    private void addEvents() {
        textCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCurrency();
            }
        });
        mWalletIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickedIcon();
            }
        });
    }

    private void onClickedIcon() {
        Intent intent = new Intent(this, SelectIconActivity.class);
        intent.putExtra(SelectIconActivity.EXTRA_FOLDER,ResourceUtils.WALLET_BASEPATH);
        startActivityForResult(intent,RC_ICON_PATH);
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
                textCurrency.setText(mCurrencyFormat.getCurrencyName());
                mTextAmount.setCurrencyCode(mCurrencyFormat.getCurrencyCode());
                mTextAmount.setText(String.valueOf(mTextAmount.getCleanDoubleValue()));
                dialog.dismiss();
            }
        });
        builderSingle.show();
    }

    private void loadInfo(){
        txtTen.setText(mWallet.getName());
        mTextAmount.setText(String.valueOf(mWallet.getCurrentBalance()));
        mWalletIcon.setImageDrawable(ResourceUtils.getWalletIcon(mIconPath));
        textCurrency.setText(mCurrencyFormat.getCurrencyName());
        mTextAmount.setCurrencyCode(mCurrencyFormat.getCurrencyCode());
        mTextAmount.setText(String.valueOf(mWallet.getCurrentBalance()));
    }

    private void updateWallet() {
        String name = txtTen.getText().toString();
        String sotien = mTextAmount.getText().toString();
        if(name.isEmpty() ) {
            txtTen.setError("Tên không được để trống");
            txtTen.requestFocus();
            return;
        }
        if (sotien.isEmpty()){
            mTextAmount.setError("Không được để trống");
            mTextAmount.requestFocus();
            return;
        }

        initWallet();
    }

    private void initWallet() {
        double newBalance = mTextAmount.getCleanDoubleValue();
        double oldBalance = mWallet.getCurrentBalance();

        String name = txtTen.getText().toString();
        mWallet.setName(name);
        mWallet.setCurrencyCode(mCurrencyFormat.getCurrencyCode());
        mWallet.setIcon(mIconPath);

        boolean wallet_update = WalletsManager.getInstance(this).updateWallet(mWallet);

        if(wallet_update) {
            // add transaction to update wallet
            if((int) newBalance != (int) oldBalance)
            {
                TransactionEntity transaction = new TransactionEntity();
                transaction.setTransactionTime(new Date());
                transaction.setTransactionAmount(newBalance - oldBalance);
                transaction.setTransactionNote("Change Balance");
                if(newBalance > oldBalance) transaction.setCategoryId(56); // Other Income
                else transaction.setCategoryId(49); // Other Express
                transaction.setWalletId(mWallet.getWalletId());
                TransactionsManager.getInstance(this).addTransaction(transaction);
            }
            Intent intent = new Intent();
            intent.putExtra(EXTRA_WALLET,Parcels.wrap(mWallet));
            setResult(RESULT_OK,intent);
            finish();
        }
        else {
            Toast.makeText(this,"Cập nhập ví thất bại",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_save:
                updateWallet();
                break;
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onClickedBack();
        return true;
    }


    private void onClickedBack() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
