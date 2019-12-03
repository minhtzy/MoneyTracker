package com.minhtzy.moneytracker.event;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.dataaccess.EventDAOImpl;
import com.minhtzy.moneytracker.dataaccess.IEventDAO;
import com.minhtzy.moneytracker.entity.EventEntity;
import com.minhtzy.moneytracker.utilities.WalletsManager;

import org.parceler.Parcels;

import java.util.List;

public class SelectEventActivity extends AppCompatActivity implements OnEventItemInteractionListener{

    public static final String EXTRA_WALLET = "SelectEvent.wallet";
    public static final String EXTRA_EVENT = "SelectEvent.event" ;
    String mWalletId;
    List<EventEntity> mListEvent;
    RecyclerView mEventView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_event);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addControls();
        addEvents();
    }

    private void addControls() {

        mWalletId = getIntent().getStringExtra(EXTRA_WALLET);
        IEventDAO eventDAO = new EventDAOImpl(this);
        mListEvent = eventDAO.getAllAvailableEventForWallet(mWalletId);

        mEventView = findViewById(R.id.list_event);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mEventView.setLayoutManager(layoutManager);

        EventRecyclerViewAdapter adapter = new EventRecyclerViewAdapter(mListEvent,this);
        mEventView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        mEventView.addItemDecoration(dividerItemDecoration);
    }

    private void addEvents() {
    }

    @Override
    public boolean onSupportNavigateUp() {
        setResult(RESULT_CANCELED);
        finish();
        return true;
    }

    @Override
    public void onEventItemClicked(EventEntity entity) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_EVENT, Parcels.wrap(entity));
        setResult(RESULT_OK,intent);
        finish();
    }
}
