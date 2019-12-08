package com.minhtzy.moneytracker.event.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.minhtzy.moneytracker.event.EventListFragment;
import com.minhtzy.moneytracker.model.EventStatus;

public class EventPagerAdapter extends FragmentStatePagerAdapter {

    public static final int PAGE_COUNT = 2;

    public EventPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i)
        {
            case 0:
                return EventListFragment.newInstance(EventStatus.START);
            case 1:
                return EventListFragment.newInstance(EventStatus.END);
        }
        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "Đang áp dụng";
            case 1:
                return "Kết thúc";
        }
        return "";
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
