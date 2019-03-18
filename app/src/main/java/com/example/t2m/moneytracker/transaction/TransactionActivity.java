package com.example.t2m.moneytracker.transaction;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.t2m.moneytracker.R;
import com.example.t2m.moneytracker.pinnedlistview.PinnedHeaderListView;

import java.util.ArrayList;
import java.util.List;

public class TransactionActivity extends AppCompatActivity {

    private PinnedHeaderListView mLViewTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        mLViewTransaction = (PinnedHeaderListView) findViewById(R.id.list_view_transaction);
        mLViewTransaction.setAdapter(new TransactionAdapter());
        addViews();
        addViews();
        addViews();
        addViews();
        addViews();
        addViews();
        addViews();
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
