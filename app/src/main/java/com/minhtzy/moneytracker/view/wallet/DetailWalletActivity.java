package com.minhtzy.moneytracker.view.wallet;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.entity.CurrencyFormat;
import com.minhtzy.moneytracker.entity.WalletEntity;
import com.minhtzy.moneytracker.utilities.CurrencyUtils;
import com.minhtzy.moneytracker.utilities.ResourceUtils;
import com.minhtzy.moneytracker.utilities.WalletsManager;
import com.minhtzy.moneytracker.view.CurrencyTextView;

import org.parceler.Parcels;

public class DetailWalletActivity extends AppCompatActivity {

    public static final String EXTRA_WALLET_DETAIL = "DetailWalletActivity.extra";
    private static final int RC_EDIT_WALLET = 10;

    private TextView mTextWalletName;
    private TextView mTextCurrencyName;
    private CurrencyTextView mTextInitBalance;
    private CurrencyTextView mTextCurrentBalance;

    private ImageView mImgWallet;
    private ImageView mImgCurrency;

    private WalletEntity mWallet;

    private Button mBtnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_wallet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addControls();
        addEvents();
        loadInfo();
    }

    private void addControls() {
        mTextWalletName = findViewById(R.id.wallet_name);
        mTextCurrentBalance = findViewById(R.id.tv_current_balance);
        mTextInitBalance = findViewById(R.id.tv_init_balance);
        mTextCurrencyName = findViewById(R.id.currency_name);

        mImgWallet = findViewById(R.id.wallet_icon);
        mImgCurrency = findViewById(R.id.currency_icon);

        mBtnDelete = findViewById(R.id.btn_delete);

        mWallet = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_WALLET_DETAIL));
    }

    private void addEvents() {
        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDeleteWallet();
            }
        });
    }

    private void onClickDeleteWallet() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Xóa ví")
                .setMessage("Bạn có chắc chắn xóa ví này?")
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WalletsManager.getInstance(DetailWalletActivity.this).deleteWallet(mWallet);
                        setResult(RESULT_OK);
                        finish();
                    }
                })
                .setNegativeButton("Cancel", null)
                .setIcon(R.drawable.ic_input_warning)
                .show();

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorMoneyTradingPositive));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorMoneyTradingNegative));
    }

    private void loadInfo() {
        if(mWallet != null)
        {
            CurrencyFormat currencyFormat = CurrencyUtils.getInstance().getCurrrencyFormat(mWallet.getCurrencyCode());
            mTextWalletName.setText(mWallet.getName());
            mTextCurrencyName.setText(currencyFormat.getCurrencyName());

            mTextInitBalance.setCurrrencyCode(mWallet.getCurrencyCode());
            mTextCurrentBalance.setCurrrencyCode(mWallet.getCurrencyCode());

            mTextInitBalance.setText(String.valueOf(mWallet.getInitBalance()));
            mTextCurrentBalance.setText(String.valueOf(mWallet.getCurrentBalance()));

            mImgWallet.setImageDrawable(ResourceUtils.getWalletIcon(mWallet.getIcon()));
            mImgCurrency.setImageDrawable(ResourceUtils.getCurrencyIcon(currencyFormat.getCurrencyCode()));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_modifier,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_edit:
                onClickEditWallet();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onClickEditWallet() {
        Intent intent = new Intent(DetailWalletActivity.this,EditWalletActivity.class);
        intent.putExtra(EditWalletActivity.EXTRA_WALLET,Parcels.wrap(mWallet));
        startActivityForResult(intent,RC_EDIT_WALLET);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == RC_EDIT_WALLET)
        {
            if(resultCode == RESULT_OK)
            {
                mWallet = Parcels.unwrap(data.getParcelableExtra(EditWalletActivity.EXTRA_WALLET));
                loadInfo();
            }
        }
    }
}
