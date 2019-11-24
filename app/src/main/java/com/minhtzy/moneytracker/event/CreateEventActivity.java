package com.minhtzy.moneytracker.event;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.adapter.WalletListAdapter;
import com.minhtzy.moneytracker.dataaccess.EventDAOImpl;
import com.minhtzy.moneytracker.dataaccess.IEventDAO;
import com.minhtzy.moneytracker.entity.EventEntity;
import com.minhtzy.moneytracker.entity.WalletEntity;
import com.minhtzy.moneytracker.model.MTDate;
import com.minhtzy.moneytracker.utilities.ResourceUtils;
import com.minhtzy.moneytracker.utilities.WalletsManager;
import com.minhtzy.moneytracker.view.SelectCategoryIconActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateEventActivity extends AppCompatActivity {

    private static final int RC_REQUEST_CATEGORY_ICON = 10;
    EditText mTextName;
    TextView mTextTime;
    TextView mTextCurrency;
    TextView mTextWallet;
    ImageView mImageIcon;
    ImageView mImageWallet;

    List<WalletEntity> mListWallet;
    WalletEntity mCurrentWallet;

    private MTDate mTimeEnd;
    private String mIconSrc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addControls();
        addEvents();
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
                onClickedAdd();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void addControls() {
        mTextName = findViewById(R.id.name_event);
        mTextTime = findViewById(R.id.txt_time_event);
        mTextCurrency = findViewById(R.id.currency);
        mTextWallet = findViewById(R.id.account);
        mImageIcon = findViewById(R.id.cate_icon);
        mImageWallet = findViewById(R.id.img_wallet);
        mTimeEnd = new MTDate();

        mListWallet = WalletsManager.getInstance(this).getAllWallet();
        mIconSrc = "";
    }

    private void addEvents() {
        mImageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.pageSetTimeEvent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickedDate(v);
            }
        });
        findViewById(R.id.pageAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickedWallet(v);
            }
        });

        mImageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickedIcon(v);
            }
        });
    }

    private void onClickedIcon(View v) {
        Intent intent = new Intent(this, SelectCategoryIconActivity.class);
        startActivityForResult(intent,RC_REQUEST_CATEGORY_ICON);

    }

    private void onClickedAdd() {
        EventEntity entity = new EventEntity();
        entity.setEventName(mTextName.getText().toString());
        entity.setTimeExpire(mTimeEnd);
        entity.setEventIcon(mIconSrc);
        entity.setLockWallet(mCurrentWallet.getWalletId());
        IEventDAO eventDAO = new EventDAOImpl(this);
        boolean inserted = eventDAO.insert(entity);
        if(inserted)
        {
            Toast.makeText(this,"Thêm sự kiện thành công.",Toast.LENGTH_SHORT).show();
            finish();
        }
        else
        {
            Toast.makeText(this,"Thêm sự kiện thất bại.",Toast.LENGTH_SHORT).show();
        }
    }

    private void onClickedDate(View v) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mTimeEnd.setYear(year);
                mTimeEnd.setMonth(month);
                mTimeEnd.setDate(dayOfMonth);
                updateLabelDate();
            }
        };

        int year = mTimeEnd.getYear();
        int month = mTimeEnd.getMonth();
        int date = mTimeEnd.getDayOfMonth();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,dateSetListener,year,month,date);
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
        datePickerDialog.show();
    }

    private void onClickedWallet(View v) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setIcon(R.drawable.ic_account_balance_wallet_black_24dp);
        builderSingle.setTitle("Chọn ví của bạn");

        final ArrayAdapter arrayAdapter = new WalletListAdapter(this, R.layout.custom_item_wallet,mListWallet);

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
        if(DateUtils.isToday(mTimeEnd.getMillis())) {
            mTextTime.setText(getResources().getString(R.string.today));
        }
        else {
            String strDate = mTimeEnd.toIsoDateString();
            mTextTime.setText(strDate);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == RC_REQUEST_CATEGORY_ICON)
        {
            if(resultCode == RESULT_OK)
            {
                mIconSrc = data.getStringExtra(SelectCategoryIconActivity.EXTRA_ICON_PATH);
                updateUI();
            }
        }
    }


    private void updateUI() {

        if(mCurrentWallet != null) {
            mTextWallet.setText(mCurrentWallet.getName());
        }
        if(!mIconSrc.isEmpty()) {
            mImageIcon.setImageDrawable(ResourceUtils.getCategoryIcon(mIconSrc));
        }
    }
}
