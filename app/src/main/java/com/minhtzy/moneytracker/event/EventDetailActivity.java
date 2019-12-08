package com.minhtzy.moneytracker.event;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.dataaccess.EventDAOImpl;
import com.minhtzy.moneytracker.dataaccess.IEventDAO;
import com.minhtzy.moneytracker.dataaccess.ITransactionsDAO;
import com.minhtzy.moneytracker.dataaccess.TransactionsDAOImpl;
import com.minhtzy.moneytracker.entity.EventEntity;
import com.minhtzy.moneytracker.entity.TransactionEntity;
import com.minhtzy.moneytracker.entity.WalletEntity;
import com.minhtzy.moneytracker.model.EventStatus;
import com.minhtzy.moneytracker.model.MTDate;
import com.minhtzy.moneytracker.transaction.ViewTransactionListActivity;
import com.minhtzy.moneytracker.utilities.ResourceUtils;
import com.minhtzy.moneytracker.utilities.WalletsManager;

import org.parceler.Parcels;

import java.util.List;

public class EventDetailActivity extends AppCompatActivity {

    public static final String EXTRA_EVENT = "EventDetail.event.extra";
    private TextView mTextName;
    private TextView mTextDate;
    private TextView mTextRemain;
    private TextView mTextWallet;

    private ImageView mImageIcon;
    private ImageView mImageWallet;

    private Button mBtnMarkFinished;
    private Button mBtnViewTransaction;

    private EventEntity mEventEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addControls();
        addEvents();
        loadDetail();
        mEventEntity = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_EVENT));
    }

    private void loadDetail() {
        if(mEventEntity != null)
        {
            mTextName.setText(mEventEntity.getEventName());
            mTextDate.setText(mEventEntity.getTimeExpire().toIsoDateString());
            mTextRemain.setText(String.format("Còn %d ngày",getRemainDate(mEventEntity.getTimeExpire())));

            WalletEntity wallet = WalletsManager.getInstance(this).getWalletById(mEventEntity.getLockWallet());
            if(wallet != null)
            {
                mTextWallet.setText(wallet.getName());
                mImageWallet.setImageDrawable(ResourceUtils.getWalletIcon(wallet.getIcon()));
            }

            mImageIcon.setImageDrawable(ResourceUtils.getCategoryIcon(mEventEntity.getEventIcon()));
        }
    }

    private int getRemainDate(MTDate timeExpire) {
        int remain =(int)( (timeExpire.getMillis() - new MTDate().getMillis()) / (24 * 60 * 60 * 1000));
        if(remain < 0) remain = 0;
        return remain;
    }

    private void addEvents() {
        mBtnMarkFinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMarkFinished();
            }
        });

        mBtnViewTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewTransaction();
            }
        });
    }

    private void onViewTransaction() {
        ITransactionsDAO iTransactionsDAO = new TransactionsDAOImpl(this);
        List<TransactionEntity> transactionList = iTransactionsDAO.getAllTransactionForEvent(mEventEntity.getEventId());
        Intent intent = new Intent(this,ViewTransactionListActivity.class);
        intent.putExtra(ViewTransactionListActivity.BUNDLE_LIST_ITEM,Parcels.wrap(transactionList));
        startActivity(intent);
    }

    private void onMarkFinished() {
        EventStatus eventStatus = null;
        if(mEventEntity.getStatus() == EventStatus.START)
        {
            eventStatus = EventStatus.END;
        }
        else
        {
            eventStatus = EventStatus.START;
        }
        IEventDAO eventDAO = new EventDAOImpl(this);
        boolean updated = eventDAO.updateStatus(mEventEntity.getEventId(), eventStatus);
        if(updated)
        {
            mEventEntity.setStatus(eventStatus);
            int id = (eventStatus == EventStatus.START) ? R.string.event_menu_mark_as_finished : R.string.event_menu_mark_not_finished;
            mBtnMarkFinished.setText(id);
        }
        else
        {
            Toast.makeText(this,"Cập nhập trạng thái sự kiện thất bại",Toast.LENGTH_SHORT).show();
        }
    }

    private void addControls() {
        mTextName = findViewById(R.id.title);
        mTextDate = findViewById(R.id.date);
        mTextRemain = findViewById(R.id.date_info);
        mTextWallet = findViewById(R.id.wallet_name);

        mImageIcon = findViewById(R.id.icon);
        mImageWallet = findViewById(R.id.wallet_icon);

        mBtnMarkFinished = findViewById(R.id.btnFinish);
        mBtnViewTransaction = findViewById(R.id.btnViewTransaction);

        mEventEntity = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_EVENT));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
