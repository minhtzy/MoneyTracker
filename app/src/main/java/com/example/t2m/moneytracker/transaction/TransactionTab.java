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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.t2m.moneytracker.R;
import com.example.t2m.moneytracker.dataaccess.MoneyTrackerDBHelper;
import com.example.t2m.moneytracker.model.Transaction;
import com.example.t2m.moneytracker.model.Wallet;
import com.example.t2m.moneytracker.transaction.AddTransactionActivity;
import com.example.t2m.moneytracker.transaction.TransactionListFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TransactionTab extends Fragment {

    public static final int FAB_ADD_TRANSACTION_REQUEST_CODE = 0;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private FloatingActionButton mFabAddTransaction;
    //HashMap<Date,Fragment> mTabFragment;
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
        TransactionViewPageAdapter adapter = new TransactionViewPageAdapter(getFragmentManager());
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        dbHelper = new MoneyTrackerDBHelper(container.getContext());
        mCurrentWallet = dbHelper.getAllWalletByUser(FirebaseAuth.getInstance().getCurrentUser().getUid()).get(0);
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

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadTransactions();
    }

    public void loadTransactions() {
        List<Transaction> transactions = dbHelper.getAllTransactionByWalletId(mCurrentWallet.getWalletId());
        for (Transaction transaction : transactions) {
            this.add(transaction);
        }
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
            TransactionViewPageAdapter adapter = (TransactionViewPageAdapter) mViewPager.getAdapter();
            adapter.add(fragment,title,index);
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
                this.add(transaction);
            }
        }
    }

    private void add(Transaction transaction) {
        mListTransaction.add(transaction);
        TransactionViewPageAdapter adapter = (TransactionViewPageAdapter) mViewPager.getAdapter();
        adapter.addTransaction(transaction);
    }

    class TransactionViewPageAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public TransactionViewPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mFragmentList.get(i);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void add(Fragment fragment,String title,int index) {
            mFragmentList.add(index,fragment);
            mFragmentTitleList.add(index,title);
            notifyDataSetChanged();
        }

        public void addTransactionTitle(Transaction transaction) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yyyy");
            String title = simpleDateFormat.format(transaction.getTransactionDate());

            int index = 0;
            boolean is_need_add = true;
            for(String t : mFragmentTitleList) {
                int c = t.compareTo(title);
                if(c == 0) {
                    is_need_add = false;
                    break;
                }else if (c > 0) {
                    try {
                        TransactionListFragment fragment = TransactionListFragment.class.newInstance();
                        add(fragment,title,index);
                        is_need_add = false;
                        break;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (java.lang.InstantiationException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    index++;
                }
            }
            if(is_need_add) {
                try {
                    TransactionListFragment fragment = TransactionListFragment.class.newInstance();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (java.lang.InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        public void addTransaction(Transaction transaction) {
        }
    }

}
