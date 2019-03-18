package com.example.t2m.moneytracker.transaction;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.t2m.moneytracker.R;
import com.example.t2m.moneytracker.pinnedlistview.PinnedHeaderListView;

import java.util.ArrayList;

public class TransactionActivity extends AppCompatActivity {


    public static final int FAB_ADD_TRANSACTION_REQUEST_CODE = 0;
    private PinnedHeaderListView mLViewTransaction;
    private FloatingActionButton mFabAddTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        mLViewTransaction = (PinnedHeaderListView) findViewById(R.id.list_view_transaction);
        mFabAddTransaction = findViewById(R.id.fab_add_transaction);
        mLViewTransaction.setAdapter(new TransactionAdapter());

        addEvents();

        addViews();
        addViews();
        addViews();
        addViews();
        addViews();
        addViews();
        addViews();
    }

    private void addEvents() {

        mFabAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionActivity.this,TransactionAdapter.AddTransaction.class);
                startActivityForResult(intent,FAB_ADD_TRANSACTION_REQUEST_CODE);
            }
        });
    }

    private void addViews() {

        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();
        TransactionAdapter adapter = (TransactionAdapter)mLViewTransaction.getAdapter();
        ArrayList<Transaction> list_transac = new ArrayList<>();
        list_transac.add(transaction1);
        list_transac.add(transaction2);
        adapter.add(transaction1.getmTransactionDate(),list_transac);

    }
}
