package com.minhtzy.moneytracker.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.minhtzy.moneytracker.MainActivity;
import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.dataaccess.ITransactionsDAO;
import com.minhtzy.moneytracker.dataaccess.IWalletsDAO;
import com.minhtzy.moneytracker.dataaccess.TransactionsDAOImpl;
import com.minhtzy.moneytracker.dataaccess.WalletsDAOImpl;
import com.minhtzy.moneytracker.model.Category;
import com.minhtzy.moneytracker.model.MTDate;
import com.minhtzy.moneytracker.model.Transaction;
import com.minhtzy.moneytracker.model.TransactionTypes;
import com.minhtzy.moneytracker.model.Wallet;
import com.minhtzy.moneytracker.view.CurrencyEditText;
import com.google.firebase.auth.FirebaseAuth;

public class AddWalletActivity extends AppCompatActivity {

    EditText txtTen;
    CurrencyEditText txtSotien;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wallet);
        // add back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addControls();
        addEvents();
    }

    private void addEvents() {

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
        ITransactionsDAO iTransactionsDAO = new TransactionsDAOImpl(this);
        Wallet wallet = new Wallet.WalletBuilder()
                .setWalletName(name)
                .setCurrentBalance(soTien)
                .setUserUID(FirebaseAuth.getInstance().getUid())
                .setCurrencyCode("VND")
                .setWalletType(1)
                .setImageSrc("")
                .build();
        boolean added_wallet = iWalletsDAO.insertWallet(wallet);

        Category category = new Category();
        category.setId(56);
        category.setType(TransactionTypes.INCOME.getValue());
        Transaction transaction = new Transaction.TransactionBuilder()
                .setTransactionDate(new MTDate().toDate())
                .setMoneyTrading(soTien)
                .setTransactionNote("Initial Wallet")
                .setCategory(category)
                .setWallet(wallet)
                .setCurrencyCode("VND")
                .build();
        boolean added_transactin =iTransactionsDAO.insertTransaction(transaction);
        if(added_wallet && added_transactin) {
            Intent intent = new Intent(AddWalletActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(this,"Khởi tạo ví thất bại",Toast.LENGTH_SHORT).show();
            if(added_wallet) {
                iWalletsDAO.deleteWallet(wallet.getWalletId());
            }
            else if(added_transactin) {
                iTransactionsDAO.deleteTransaction(transaction);
            }
        }
    }

    private void addControls() {
        //
        txtTen = (EditText)findViewById(R.id.txtTen);
        txtSotien = (CurrencyEditText) findViewById(R.id.txtSotien);
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
