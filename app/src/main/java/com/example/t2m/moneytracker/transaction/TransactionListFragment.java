package com.example.t2m.moneytracker.transaction;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.t2m.moneytracker.R;
import com.example.t2m.moneytracker.adpter.TransactionAdapter;
import com.example.t2m.moneytracker.model.Transaction;
import com.example.t2m.moneytracker.pinnedlistview.PinnedHeaderListView;

import java.util.ArrayList;

public class TransactionListFragment extends Fragment {

    private PinnedHeaderListView mLViewTransaction;
    private TransactionAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_transaction,container,false);
        mLViewTransaction = (PinnedHeaderListView) view.findViewById(R.id.list_view_transaction);
        mLViewTransaction.setAdapter(new TransactionAdapter());

        addEvents();

        addViews();
        //addViews();addViews();addViews();
        return view;
    }

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_transaction);
//        mLViewTransaction = (PinnedHeaderListView) findViewById(R.id.list_view_transaction);
//        mFabAddTransaction = findViewById(R.id.fab_add_transaction);
//        mLViewTransaction.setAdapter(new TransactionAdapter());
//
//        addEvents();
//
//       addViews();
//    }

    public void addTransaction(Transaction transaction) {
        TransactionAdapter adapter = (TransactionAdapter)mLViewTransaction.getAdapter();
        adapter.add(transaction);
    }

    private void addEvents() {

    }

    private void addViews() {

        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();
        TransactionAdapter adapter = (TransactionAdapter)mLViewTransaction.getAdapter();
//        ArrayList<Transaction> list_transac = new ArrayList<>();
//        list_transac.add(transaction1);
//        list_transac.add(transaction2);
        adapter.add(transaction1);
        adapter.add(transaction2);

    }
}
