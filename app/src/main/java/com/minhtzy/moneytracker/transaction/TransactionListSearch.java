package com.minhtzy.moneytracker.transaction;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minhtzy.moneytracker.R;

import com.minhtzy.moneytracker.adapter.TransactionListAdapter;
import com.minhtzy.moneytracker.common.Constants;
import com.minhtzy.moneytracker.entity.TransactionEntity;
import com.minhtzy.moneytracker.model.MTDate;
import com.minhtzy.moneytracker.pinnedlistview.PinnedHeaderListView;
import com.minhtzy.moneytracker.utilities.CurrencyUtils;


import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionListSearch extends Fragment {

    private static final String LOG_TAG = "TransactionList.LOG_TAG";
    private PinnedHeaderListView mLViewTransaction;
    private TransactionListAdapter mAdapter;
    private LinearLayout mBlankLayout;
    List<TransactionEntity> mItems;
    List<Pair<Date,List<TransactionEntity>>> mFilterItems;
    View headerView;

    private static final String BUNDLE_LIST_ITEM = "TransactionListFragment.bundle.list_items";

    public static TransactionListSearch newInstance(List<TransactionEntity> items) {
        Log.d(LOG_TAG,"create new instance "+ items.size());
        TransactionListSearch fragment = new TransactionListSearch();
        fragment.setItems(items);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_LIST_ITEM,(ArrayList<TransactionEntity>)items);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(mItems == null){
            if(savedInstanceState != null) {
                mItems =(ArrayList<TransactionEntity>) Parcels.unwrap(savedInstanceState.getParcelable(BUNDLE_LIST_ITEM));
            }
            else {
                mItems = new ArrayList<>();
            }
        }
        Log.d(LOG_TAG,"create view items " + mItems.size());

        View view = inflater.inflate(R.layout.fragment_list_transaction,container,false);
        mLViewTransaction = view.findViewById(R.id.list_view_transaction);
        mBlankLayout = view.findViewById(R.id.layout_transaction_empty);
        mFilterItems = new ArrayList<>();
        mAdapter = new TransactionListAdapter(this.getContext(),mFilterItems);
        mLViewTransaction.setAdapter(mAdapter);
//        headerView = inflater.inflate(
//                R.layout.header_transaction_statistics, null, false);
//        mLViewTransaction.addHeaderView(headerView);

        mLViewTransaction.setOnItemClickListener(new PinnedHeaderListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int section, int position, long id) {
                TransactionEntity transaction = (TransactionEntity) mAdapter.getItem(section,position);
                onClickItem(transaction);
            }

            @Override
            public void onSectionClick(AdapterView<?> adapterView, View view, int section, long id) {

            }
            @Override
            public void onHeaderClick(AdapterView<?> adapterView, View view, int section, long id) {

            }
        });
        new loadTransactions(getActivity()).execute();
        return view;
    }

    private void onClickItem(TransactionEntity transaction) {
        Intent data = new Intent(TransactionListSearch.this.getContext(), ViewTransactionDetailActivity.class);
        data.putExtra(ViewTransactionDetailActivity.EXTRA_TRANSACTION, Parcels.wrap(transaction));
        startActivity(data);
    }

    public void setItems(List<TransactionEntity> items) {
        mItems = items;
    }

    public void add(TransactionEntity transaction) {
        mItems.add(transaction);
        new loadTransactions(getActivity()).execute();
    }

    private void filterPairTransactions(List<TransactionEntity> transactions) {
        for(TransactionEntity transaction : transactions) {
            filterPairTransaction(transaction);
        }
    }

    private void filterPairTransaction(TransactionEntity transaction) {
        int index = -1;
        MTDate date = new MTDate(transaction.getTransactionTime());
        for (int i = 0; i < mFilterItems.size(); ++i) {
            MTDate dateI = new MTDate(mFilterItems.get(i).first);
            if (compareDate(date,dateI) == 0) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            MTDate dateI = new MTDate(mFilterItems.get(index).first);
            if (compareDate(date,dateI) == 0) {
                mFilterItems.get(index).second.add(0, transaction);
            }
        }
        else {
            index = 0;
            for (int i = 0; i < mFilterItems.size(); ++i) {
                MTDate dateI = new MTDate(mFilterItems.get(i).first);
                if (compareDate(date,dateI) > 0) {
                    index = i;
                    break;
                }
            }
            ArrayList<TransactionEntity> trans = new ArrayList<>();
            trans.add(transaction);
            mFilterItems.add(index, new Pair<Date, List<TransactionEntity>>(transaction.getTransactionTime(), trans));
        }
    }

    private int compareDate(MTDate date1,MTDate date2) {
        return date1.setTimeToBeginningOfDay().getCalendar().compareTo(date2.setTimeToBeginningOfDay().getCalendar());

    }

    // =====================================================


    protected class loadTransactions extends AsyncTask<Void, Void, Void> {
        private Dialog mDialog;
        private Activity activity;

        private loadTransactions(Activity activity) {
            this.activity = activity;
        }
        @Override
        protected void onPreExecute() {
            mDialog = new Dialog(activity);
            // chu y phai dat truoc setcontentview
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialog.setContentView(R.layout.loading_view);

            mDialog.setCancelable(false);
            mDialog.show();
        }

        protected Void doInBackground(Void... unused) {

            // su dung phuong thuc de update lai adapter
            activity.runOnUiThread(runnableUpdateAdapter);

            return (null);
        }

        protected void onPostExecute(Void unused) {

            mDialog.dismiss();
        }
    }


    private Runnable runnableUpdateAdapter = new Runnable() {

        @Override
        public void run() {

            updateUI();
        }
    };

    private void updateUI() {
        if(mItems.size() == 0) {
            mLViewTransaction.setVisibility(View.INVISIBLE);
            mLViewTransaction.removeHeaderView(headerView);
            mBlankLayout.setVisibility(View.VISIBLE);
        }
        else{
            mBlankLayout.setVisibility(View.INVISIBLE);
            if (headerView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                headerView = inflater.inflate(
                        R.layout.header_transaction_statistics, null, false);
            }
            mLViewTransaction.addHeaderView(headerView);
            updateHeaderView();
            // thuc hien update lai adapter
            mFilterItems.clear();
            filterPairTransactions(mItems);
            mAdapter.updateValues(mFilterItems);
            mLViewTransaction.setVisibility(View.VISIBLE);

        }
    }
    private void updateHeaderView() {

        float income = 0;
        float express= 0;

        if(headerView  != null) {
            for(TransactionEntity tran : mItems) {
                if(tran.getTransactionAmount() < 0) {
                    express += Math.abs(tran.getTransactionAmount());
                }
                else {
                    income += Math.abs(tran.getTransactionAmount());
                }
            }

            express = Math.abs(express);
            income = Math.abs(income);
            TextView textIncome = headerView.findViewById(R.id.fts_so_du_dau);
            TextView textExpress = headerView.findViewById(R.id.fts_so_du_cuoi);
            TextView textRemain = headerView.findViewById(R.id.fts_con_lai);

            textIncome.setText(String.valueOf(income));
            textExpress.setText(String.valueOf(express));
            textRemain.setText(String.valueOf(income-express));
        }
    }

}
