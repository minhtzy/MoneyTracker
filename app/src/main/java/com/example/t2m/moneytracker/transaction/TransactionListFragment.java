package com.example.t2m.moneytracker.transaction;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.t2m.moneytracker.R;
import com.example.t2m.moneytracker.adapter.ListTransactionAdapter;
import com.example.t2m.moneytracker.model.Transaction;
import com.example.t2m.moneytracker.pinnedlistview.PinnedHeaderListView;
import com.example.t2m.moneytracker.utilities.DateUtils;
import com.example.t2m.moneytracker.utilities.TransactionsManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionListFragment extends Fragment {

    private PinnedHeaderListView mLViewTransaction;
    private ListTransactionAdapter mAdapter;
    List<Pair<Date,List<Transaction>>> mItems;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_transaction,container,false);
        mLViewTransaction = (PinnedHeaderListView) view.findViewById(R.id.list_view_transaction);
        mItems = new ArrayList<>();
        mAdapter = new ListTransactionAdapter(mItems);
        mLViewTransaction.setAdapter(mAdapter);
        addEvents();
        filterTransactions();
        return view;
    }

    private void filterTransactions() {
        mItems.clear();
        TransactionsManager transactionsManager = TransactionsManager.getInstance(this.getContext());
        //transactionsManager.setCurrentWallet(WalletsManager.getInstance(this.getContext()).getCurrentWallet());
        for(Transaction tran : transactionsManager.getAllTransactions()) {
            add(tran);
        }
        mAdapter.notifyDataSetChanged();
    }
    public void add(Transaction transaction) {
        int index = 0;
        for (int i = 0; i < mItems.size(); ++i) {
            if(DateUtils.compareDate(mItems.get(i).first,transaction.getTransactionDate()) < 0) {
                index = i;
                break;
            }
        }
        if(index < mItems.size() && DateUtils.compareDate(mItems.get(index).first,transaction.getTransactionDate()) == 0) {
            mItems.get(index).second.add(0,transaction);
        }
        else {
            ArrayList<Transaction> transactions = new ArrayList<>();
            transactions.add(transaction);
            mItems.add(index, new Pair<Date, List<Transaction>>(transaction.getTransactionDate(), transactions));

        }
    }
    public void add(Date date,List<Transaction> list_item) {
        mItems.add(new Pair<Date, List<Transaction>>(date,list_item));
    }

    private void addEvents() {

    }

}
