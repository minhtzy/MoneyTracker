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
import com.minhtzy.moneytracker.dataaccess.ITransactionsDAO;
import com.minhtzy.moneytracker.dataaccess.TransactionsDAOImpl;
import com.minhtzy.moneytracker.entity.TransactionEntity;
import com.minhtzy.moneytracker.entity.WalletEntity;
import com.minhtzy.moneytracker.model.DateRange;
import com.minhtzy.moneytracker.model.MTDate;
import com.minhtzy.moneytracker.pinnedlistview.PinnedHeaderListView;
import com.minhtzy.moneytracker.utilities.WalletsManager;


import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionListFragment extends Fragment {

    private static final String LOG_TAG = "TransactionList.LOG_TAG";
    private static final int REQUEST_VIEW_DETAIL = 100;
    private PinnedHeaderListView mLViewTransaction;
    private TransactionListAdapter mAdapter;
    private LinearLayout mBlankLayout;
    private List<TransactionEntity> mItems;
    private WalletEntity mWallet;
    private DateRange mDateRange;
    List<Pair<Date,List<TransactionEntity>>> mFilterItems;
    View headerView;

    private static final String BUNDLE_LIST_ITEM = "TransactionListFragment.bundle.list_items";
    private static final String BUNDLE_DATE_RANGE = "TransactionListFragment.bundle.dateRange";
    public static final String BUNDLE_WALLET = "TransactionListFragment.bundle.wallet";
    public static TransactionListFragment newInstance(List<TransactionEntity> items) {
        Log.d(LOG_TAG,"create new instance "+ items.size());
        TransactionListFragment fragment = new TransactionListFragment();
        fragment.setItems(items);
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_LIST_ITEM,Parcels.wrap(items));
        fragment.setArguments(bundle);
        return fragment;
    }

    public static TransactionListFragment newInstance(WalletEntity wallet,DateRange dateRange) {
        Log.d(LOG_TAG,"create new instance "+ dateRange);
        TransactionListFragment fragment = new TransactionListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_DATE_RANGE,dateRange);
        bundle.putParcelable(BUNDLE_WALLET,Parcels.wrap(wallet));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
        {
            mItems =(ArrayList<TransactionEntity>) Parcels.unwrap(getArguments().getParcelable(BUNDLE_LIST_ITEM));
            mWallet = Parcels.unwrap(getArguments().getParcelable(BUNDLE_WALLET));
            mDateRange = (DateRange) getArguments().getSerializable(BUNDLE_DATE_RANGE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(mItems == null){
            if(mWallet != null && mDateRange != null)
            {

                ITransactionsDAO iTransactionsDAO = new TransactionsDAOImpl(inflater.getContext());

                mItems = iTransactionsDAO.getAllTransactionByPeriod(mWallet.getWalletId(),mDateRange);
            }
            else
            {
                mItems= new ArrayList<>();
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
        new loadTransactions(this.getActivity()).execute();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //new loadTransactions(this.getActivity()).execute();
    }

    private void onClickItem(TransactionEntity transaction) {
        Intent data = new Intent(TransactionListFragment.this.getContext(),ViewTransactionDetailActivity.class);
        data.putExtra(ViewTransactionDetailActivity.EXTRA_TRANSACTION, Parcels.wrap(transaction));
        startActivityForResult(data,REQUEST_VIEW_DETAIL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_VIEW_DETAIL) {

        }
    }

    public void setItems(List<TransactionEntity> items) {
        mItems = items;
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
            //mDialog.show();
        }

        protected Void doInBackground(Void... unused) {

            // su dung phuong thuc de update lai adapter
            activity.runOnUiThread(runnableUpdateAdapter);

            return (null);
        }

        protected void onPostExecute(Void unused) {

            //mDialog.dismiss();
        }
    }


    private Runnable runnableUpdateAdapter = new Runnable() {

        @Override
        public void run() {

            updateUI();
        }
    };

    public void updateUI() {
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
