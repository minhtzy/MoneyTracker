package com.minhtzy.moneytracker.view.event;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.view.event.adapter.EventPagerAdapter;

public class EventFragment extends Fragment {

    private static final int RC_ADD_EVENT = 10 ;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private FloatingActionButton mFabAddEvent;
    private EventPagerAdapter mAdapter;
    public EventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        mTabLayout = view.findViewById(R.id.tab_layout);
        mViewPager = view.findViewById(R.id.page_view);
        mFabAddEvent = view.findViewById(R.id.fab_add_event);

        mAdapter = new EventPagerAdapter(getChildFragmentManager());

        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        addEvents();
        return view;
    }

    private void addEvents() {
        mFabAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),CreateEventActivity.class);
                startActivityForResult(intent,RC_ADD_EVENT);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_ADD_EVENT)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
