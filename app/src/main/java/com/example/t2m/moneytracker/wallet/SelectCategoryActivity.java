package com.example.t2m.moneytracker.wallet;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.t2m.moneytracker.R;
import com.example.t2m.moneytracker.adapter.CategoriesPagerAdapter;
import com.example.t2m.moneytracker.model.Category;

public class SelectCategoryActivity extends AppCompatActivity implements ListCategoryFragment.OnCategoryFragmenListener {

    public static final String EXTRA_CATEGORY = "SelectCategoryActivity.Extra.Category";
    Button btnCancle;
    TabLayout mTabLayout;
    ViewPager mViewPager;
    PagerAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);
        addControls();
        addEvents();
    }


    private void addEvents() {

//        btnCancle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        mAdapter = new CategoriesPagerAdapter(this.getSupportFragmentManager(),this);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setScrollPosition(1,0f,true);
        mViewPager.setCurrentItem(1);
    }

    private void addControls() {

        mTabLayout = findViewById(R.id.tab_layout_categories);
        mViewPager = findViewById(R.id.view_pager_categories);
    }


    @Override
    public void onItemClicked(Category category) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_CATEGORY,category);
        setResult(RESULT_OK,intent);
        finish();
    }
}
