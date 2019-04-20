package com.example.t2m.moneytracker.transaction;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.t2m.moneytracker.R;
import com.example.t2m.moneytracker.adapter.WalletListAdapter;
import com.example.t2m.moneytracker.dataaccess.ITransactionsDAO;
import com.example.t2m.moneytracker.dataaccess.IWalletsDAO;
import com.example.t2m.moneytracker.dataaccess.TransactionsDAOImpl;
import com.example.t2m.moneytracker.dataaccess.WalletsDAOImpl;
import com.example.t2m.moneytracker.model.Category;
import com.example.t2m.moneytracker.model.Transaction;
import com.example.t2m.moneytracker.model.TransactionType;
import com.example.t2m.moneytracker.model.Wallet;
import com.example.t2m.moneytracker.utilities.TransactionsManager;
import com.example.t2m.moneytracker.wallet.SelectCategoryActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddTransactionActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_CATEGORY = 1;
    public static final String EXTRA_TRANSACTION = "com.example.t2m.moneytracker.extra.transaction";
    private EditText mTextMoney;
    private EditText mTextCategory;
    private EditText mTextNote;
    private EditText mTextDate;
    private EditText mTextWallet;
    private Button mBtnAdd;
    private Button mBtnCancle;
    private Calendar mCalendar;

    private FirebaseUser mCurrentUser;
    private IWalletsDAO iWalletsDAO;
    private ITransactionsDAO iTransactionsDAO;
    private List<Wallet> mListWallet;
    private TransactionType mCurrentTransactionType =null;
    private Wallet mCurrentWallet = null;
    private Category mCurrentCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        addControls();
        updateLabelDate();
        addEvents();
    }

    private void addControls() {
        mTextMoney = findViewById(R.id.text_transaction_money);
        mTextCategory = findViewById(R.id.text_transaction_category);
        mTextNote = findViewById(R.id.text_transaction_note);
        mTextDate = findViewById(R.id.text_transaction_date);
        mTextWallet = findViewById(R.id.text_transaction_wallet);
        mBtnAdd = findViewById(R.id.buttonSave);
        mBtnCancle = findViewById(R.id.buttonCancle);
        mCalendar = Calendar.getInstance();
        iWalletsDAO = new WalletsDAOImpl(this);
        iTransactionsDAO = new TransactionsDAOImpl(this);
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mListWallet = iWalletsDAO.getAllWalletByUser(mCurrentUser.getUid());
    }

    private void addEvents() {
        mTextCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickedCategory(v);
            }
        });
        mTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickedDate(v);
            }
        });
        mTextWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickedWallet(v);
            }
        });
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickedAdd(v);
            }
        });
        mBtnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickedCancle(v);
            }
        });
    }

    private void onClickedAdd(View v) {
        if(mCurrentWallet == null) {
            mTextWallet.setError("Chọn ví của bạn");
            mTextWallet.requestFocus();
            return;
        }
        if(mCurrentTransactionType == null) {
            mTextCategory.setError("Chọn kiểu giao dịch");
            mTextCategory.requestFocus();
            return;
        }
        float money = Float.parseFloat(mTextMoney.getText().toString());
        String note = mTextNote.getText().toString();
        Date date = mCalendar.getTime();
        Transaction transaction = new Transaction.TransactionBuilder()
                .setTransactionDate(date)
                .setTransactionType(mCurrentTransactionType)
                .setWallet(mCurrentWallet)
                .setCurrencyCode(mCurrentWallet.getCurrencyCode())
                .setMoneyTrading(money)
                .setTransactionNote(note)
                .build();

        TransactionsManager.getInstance(this).addTransaction(transaction);

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TRANSACTION,transaction);
        setResult(RESULT_OK,intent);
        finish();
    }
    private void onClickedCancle(View v) {
        finish();
    }

    private void onClickedCategory(View v) {
        Intent intent = new Intent(this,SelectCategoryActivity.class);
        startActivityForResult(intent,REQUEST_CODE_CATEGORY);
    }
    private void onClickedDate(View v) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mCalendar.set(year,month,dayOfMonth);
                updateLabelDate();
            }
        };
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int date = mCalendar.get(Calendar.DATE);
        new DatePickerDialog(this,dateSetListener,year,month,date).show();
    }

    private void onClickedWallet(View v) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setIcon(R.drawable.ic_account_balance_wallet_black_24dp);
        builderSingle.setTitle("Chọn ví của bạn");

        final ArrayAdapter arrayAdapter = new WalletListAdapter(this, R.layout.custom_item_category,mListWallet);


        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCurrentWallet = mListWallet.get(which);
                updateUI();
                dialog.dismiss();
            }
        });
        builderSingle.show();
    }

    private void updateLabelDate() {
        Date date = mCalendar.getTime();
        if(DateUtils.isToday(date.getTime())) {
            mTextDate.setText("Hôm nay");
        }
        else {
            String myFormat = "dd/MM/yyyy";
            SimpleDateFormat sdformat = new SimpleDateFormat(myFormat);
            mTextDate.setText(sdformat.format(mCalendar.getTime()));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_CATEGORY) {
            if(resultCode == RESULT_OK) {

                mCurrentCategory =(Category) data.getSerializableExtra(SelectCategoryActivity.EXTRA_CATEGORY);

                updateUI();
            }
        }
    }

    private void updateUI() {
        if(mCurrentTransactionType != null) {
            mTextCategory.setText(mCurrentTransactionType.getCategory());
            ImageView imageView = findViewById(R.id.image_transaction_category);

            // lấy ảnh từ asset
            String base_path = "category/";
            try {
                Drawable img = Drawable.createFromStream(this.getAssets().open(base_path + mCurrentCategory.getIcon()),null);
                imageView.setImageDrawable(img);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(mCurrentWallet != null) {
            mTextWallet.setText(mCurrentWallet.getWalletName());
            ImageView imageView = findViewById(R.id.image_transaction_wallet);
            int resourceId = getResources().getIdentifier(
                    mCurrentWallet.getImageSrc(),
                    "drawable",
                    "com.example.t2m.moneytracker"
            );
            imageView.setImageResource(resourceId);
        }
    }
}