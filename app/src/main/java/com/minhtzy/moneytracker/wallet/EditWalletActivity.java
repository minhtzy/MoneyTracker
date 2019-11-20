package com.minhtzy.moneytracker.wallet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.view.CurrencyEditText;

public class EditWalletActivity extends AppCompatActivity {

    public static final String EXTRA_WALLET = "com.minhtzy.moneytracker.extra.wallet";
    private  EditText txtTen;
    CurrencyEditText txtSotien;
    Wallet wallet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_wallet);
        // add back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addControls();
        addEvents();
    }

    private void addControls() {
        txtTen = (EditText)findViewById(R.id.txtTen);
        txtSotien = (CurrencyEditText)findViewById(R.id.txtSotien);
        wallet = (Wallet) getIntent().getSerializableExtra(EXTRA_WALLET);
        updateUI();
    }

    private void updateUI() {
        if(wallet != null)
        {
            txtTen.setText(wallet.getWalletName());
            txtSotien.setText(String.valueOf(wallet.getCurrentBalance()));
        }
    }

    private void addEvents() {

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
                saveWallet();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveWallet() {
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
