package com.example.t2m.moneytracker.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Pair;

import java.util.List;

public class TransactionTabAdapter extends FragmentPagerAdapter {
    private final List<Pair<String,Fragment>> mFragmentList;

    public TransactionTabAdapter(FragmentManager fm,List<Pair<String,Fragment>> fragmentList) {
        super(fm);
        mFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int i) {
        return mFragmentList.get(i).second;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentList.get(position).first;
    }
}
