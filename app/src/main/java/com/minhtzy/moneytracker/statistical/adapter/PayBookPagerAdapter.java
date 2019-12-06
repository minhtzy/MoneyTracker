package com.minhtzy.moneytracker.statistical.adapter;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.minhtzy.moneytracker.entity.TransactionEntity;
import com.minhtzy.moneytracker.statistical.FragmentPagerPB;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class PayBookPagerAdapter extends FragmentStatePagerAdapter {


    static final int CATE_DEBT_ID = 59;
    static final int CATE_LOAN_ID = 57;

    private List<TransactionEntity> mItems = new ArrayList<>();

    private String[] mTitles = {"CẦN TRẢ", "CẦN THU"};

    public PayBookPagerAdapter(FragmentManager fm, List<TransactionEntity> listTransaction) {
        super(fm);

        if (listTransaction != null) {

            mItems.addAll(listTransaction);

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public Fragment getItem(int position) {

        List<TransactionEntity> itemsDebt = new ArrayList<>();
        List<TransactionEntity> itemsLoan = new ArrayList<>();

        for (TransactionEntity tran : mItems) {

            if (tran.getCategoryId() == CATE_DEBT_ID) {

                itemsDebt.add(tran);

            }
            if (tran.getCategoryId() == CATE_LOAN_ID) {

                itemsLoan.add(tran);

            }
        }

        switch (position) {

            case 0:
                return FragmentPagerPB.newInstance(itemsDebt, CATE_DEBT_ID);

            case 1:
                return FragmentPagerPB.newInstance(itemsLoan, CATE_LOAN_ID);
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return mTitles[position];
    }
}
