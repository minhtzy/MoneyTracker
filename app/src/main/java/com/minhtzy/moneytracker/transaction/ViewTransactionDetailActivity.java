package com.minhtzy.moneytracker.transaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.libraries.places.widget.Autocomplete;
import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.dataaccess.EventDAOImpl;
import com.minhtzy.moneytracker.entity.CategoryEntity;
import com.minhtzy.moneytracker.entity.EventEntity;
import com.minhtzy.moneytracker.entity.TransactionEntity;
import com.minhtzy.moneytracker.entity.WalletEntity;
import com.minhtzy.moneytracker.event.SelectEventActivity;
import com.minhtzy.moneytracker.model.Constants;
import com.minhtzy.moneytracker.model.MTDate;
import com.minhtzy.moneytracker.utilities.ResourceUtils;
import com.minhtzy.moneytracker.utilities.CategoryManager;
import com.minhtzy.moneytracker.utilities.TransactionsManager;
import com.minhtzy.moneytracker.utilities.WalletsManager;
import com.minhtzy.moneytracker.view.CurrencyTextView;

import org.parceler.Parcels;

import static com.minhtzy.moneytracker.transaction.AddTransactionActivity.REQUEST_PLACE_PICKER;

public class ViewTransactionDetailActivity extends AppCompatActivity {


    public static final String EXTRA_TRANSACTION = "Extra.TransactionDetail.Transaction";
    private TextView mTextMoney;
    private TextView mTextCategory;
    private TextView mTextNote;
    private TextView mTextDate;
    private TextView mTextWallet;
    private ImageView mImgPreview;

    private static final int REQUEST_EDIT_TRANSACTION = 1;

    private TransactionEntity mTransaction;
    private TextView mTextEvent;
    private ImageView mImgEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transaction_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(savedInstanceState != null) {
            mTransaction = (TransactionEntity) Parcels.unwrap(savedInstanceState.getParcelable(EXTRA_TRANSACTION));
        }
        else {
            mTransaction = (TransactionEntity) Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_TRANSACTION));
        }
        addControls();
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meunu_edit_delete,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete : {
                onClickedDelete();
                return true;
            }
            case R.id.action_edit : {
                onClickedEdit();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onClickedEdit() {

        Intent intent = new Intent(ViewTransactionDetailActivity.this,EditTransactionActivity.class);
        intent.putExtra(EditTransactionActivity.EXTRA_TRANSACTION, Parcels.wrap(mTransaction));
        startActivityForResult(intent,REQUEST_EDIT_TRANSACTION);
    }

    private void onClickedDelete() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Xóa giao dịch")
                .setMessage("Bạn có chắc chắn xóa giao dịch này?")
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TransactionsManager.getInstance(ViewTransactionDetailActivity.this).deleteTransaction(mTransaction);
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

    @Override
    public boolean onSupportNavigateUp() {
        onClickedCancle();
        return true;
    }

    private void addControls() {
        mTextMoney = findViewById(R.id.text_transaction_money);
        mTextCategory = findViewById(R.id.text_transaction_category);
        mTextNote = findViewById(R.id.text_transaction_note);
        mTextDate = findViewById(R.id.text_transaction_date);
        mTextWallet = findViewById(R.id.text_transaction_wallet);
        mImgPreview = findViewById(R.id.image_preview);
    }

    private void addEvents() {

    }

    private void onClickedCancle() {

        setResult(RESULT_CANCELED);
        finish();
    }

    private void updateUI() {
        if(mTransaction != null) {
            //Toast.makeText(this, "" + mTransaction.getTransactionDate(), Toast.LENGTH_LONG).show();
            CategoryEntity category = CategoryManager.getInstance().getCategoryById(mTransaction.getCategoryId());
            WalletEntity wallet = WalletsManager.getInstance(this).getWalletById(mTransaction.getWalletId());
            mTextCategory.setText(category.getCategoryName());
            ImageView imageView = findViewById(R.id.image_transaction_category);
            imageView.setImageDrawable(ResourceUtils.getCategoryIcon(category.getCategoryIcon()));

            mTextDate.setText(new MTDate(mTransaction.getTransactionTime()).toIsoDateShortTimeString());
            mTextMoney.setText(String.valueOf(mTransaction.getTransactionAmount()));
            if(mTransaction.getTransactionAmount() >= 0) {
                mTextMoney.setTextColor(getResources().getColor(R.color.colorMoneyTradingPositive));
            }
            else {
                mTextMoney.setTextColor(getResources().getColor(R.color.colorMoneyTradingNegative));
            }
            mTextNote.setText(mTransaction.getTransactionNote());
            mTextWallet.setText(wallet.getName());
            if(mTransaction.getMediaUri() != null && !mTransaction.getMediaUri().isEmpty() ) {
                updateImagePreView(mTransaction.getMediaUri());
            }

            if(mTransaction.getEventId() != Constants.NOT_SET) {
                EventEntity mEvent = new EventDAOImpl(this).getEventById(mTransaction.getEventId());
                mTextEvent.setText(mEvent.getEventName());
                mImgEvent.setImageDrawable(ResourceUtils.getCategoryIcon(mEvent.getEventIcon()));
            }
            if(mTransaction.getP)(requestCode == REQUEST_PLACE_PICKER) {
            mCurrentPlace = Autocomplete.getPlaceFromIntent(data);
            mTextPlace.setText(mCurrentPlace.getName());
            findViewById(R.id.clear_location).setVisibility(View.VISIBLE);
        }
        }
    }
    private void updateImagePreView(String uri) {
        Bitmap bitmap = ResourceUtils.loadImageFromStorage(uri);
        if(bitmap != null) {
            int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
            mImgPreview.setImageBitmap(scaled);
            mImgPreview.setVisibility(View.VISIBLE);
        }
        else {
            mImgPreview.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if(requestCode == REQUEST_EDIT_TRANSACTION) {
                mTransaction = (TransactionEntity) Parcels.unwrap(data.getParcelableExtra(EditTransactionActivity.EXTRA_TRANSACTION));
                updateUI();
            }
        }
    }
}
