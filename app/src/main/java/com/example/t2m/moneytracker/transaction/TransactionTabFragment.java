package com.example.t2m.moneytracker.transaction;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.t2m.moneytracker.R;
import com.example.t2m.moneytracker.adpter.TransactionTabAdapter;
import com.example.t2m.moneytracker.dataaccess.MoneyTrackerDBHelper;
import com.example.t2m.moneytracker.model.Transaction;
import com.example.t2m.moneytracker.model.Wallet;
import com.example.t2m.moneytracker.transaction.AddTransactionActivity;
import com.example.t2m.moneytracker.transaction.TransactionListFragment;
import com.example.t2m.moneytracker.utilities.TransactionsManager;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TransactionTabFragment extends Fragment {

    public static final int FAB_ADD_TRANSACTION_REQUEST_CODE = 0;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private TransactionTabAdapter mAdapter;

    private FloatingActionButton mFabAddTransaction;
    List<Pair<String,Fragment>> mTabFragment;
    List<Transaction> mListTransaction = new ArrayList<>();
    MoneyTrackerDBHelper dbHelper;
    Wallet mCurrentWallet = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_transaction_tab,container,false);
        mTabLayout = view.findViewById(R.id.tab_layout);
        mViewPager = view.findViewById(R.id.page_view);
        mFabAddTransaction = view.findViewById(R.id.fab_add_transaction);
        addEvents();
        mTabFragment = new ArrayList<>();
        mAdapter = new TransactionTabAdapter(getFragmentManager(),mTabFragment);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        addTab("01/2019",0);
        addTab("02/2019",1);
        addTab("03/2019",2);
        addTab("04/2019",3);
        addTab("05/2019",4);
        addTab("06/2019",5);
        addTab("07/2019",6);
        addTab("08/2019",7);
        addTab("09/2019",8);
        addTab("10/2019",9);
        mAdapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    private void addEvents() {
        mFabAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AddTransactionActivity.class);
                startActivityForResult(intent,FAB_ADD_TRANSACTION_REQUEST_CODE);
            }
        });
    }

    private void addTab(String title,int index) {
        try {
            Fragment fragment = (Fragment) TransactionListFragment.class.newInstance();
            mTabFragment.add(index,new Pair<String, Fragment>(title,fragment));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == FAB_ADD_TRANSACTION_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                Transaction transaction = (Transaction) data.getSerializableExtra(AddTransactionActivity.EXTRA_TRANSACTION);
                this.addTransaction(transaction);
            }
        }
    }

    public void addTab(Fragment fragment, String title, int index) {
        mTabFragment.add(index,new Pair<String, Fragment>(title, fragment));
    }

    public void addTransaction(Transaction transaction) {
        TransactionsManager transactionsManager = TransactionsManager.getInstance(this.getContext());
        transactionsManager.setCurrentWallet(transaction.getWallet());
        transactionsManager.addTransaction(transaction);
        mAdapter.notifyDataSetChanged();
    }


}
