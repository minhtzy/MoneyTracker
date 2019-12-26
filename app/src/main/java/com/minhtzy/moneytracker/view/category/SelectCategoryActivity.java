package com.minhtzy.moneytracker.view.category;

import android.content.Intent;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.minhtzy.moneytracker.R;

import com.minhtzy.moneytracker.view.category.adapter.CategoriesPagerAdapter;
import com.minhtzy.moneytracker.entity.CategoryEntity;
import com.minhtzy.moneytracker.view.wallet.ListCategoryFragment;

import org.parceler.Parcels;


public class SelectCategoryActivity extends AppCompatActivity implements ListCategoryFragment.OnCategoryFragmentListener {

    public static final String EXTRA_CATEGORY = "SelectCategoryActivity.Extra.Category";
    TabLayout mTabLayout;
    ViewPager mViewPager;
    PagerAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_choose_category);
        addControls();
        addEvents();
    }


    private void addEvents() {
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
    public void onItemClicked(CategoryEntity category) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_CATEGORY, Parcels.wrap(category));
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_modifier,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit : {
                onClickedModifier();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onClickedModifier() {
        Toast.makeText(this,"Tính năng đang phát triển",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onClickedBack();
        return true;
    }

    private void onClickedBack() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
