package com.minhtzy.moneytracker.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.adapter.CategoryIconListAdapter;

public class SelectCategoryIconActivity extends AppCompatActivity implements OnIconInteractionListener {

    public static final String EXTRA_ICON_PATH = "CategoryIcon.path" ;
    RecyclerView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addControls();
        addEvents();
    }

    private void addControls() {

        mListView = findViewById(R.id.list_cat_icon);
        mListView.setLayoutManager(new GridLayoutManager(this,5));
        mListView.setAdapter(new CategoryIconListAdapter(this));
    }

    private void addEvents() {
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    @Override
    public void onCategoryIconClick(String categoryPath) {
        Intent intent = getIntent();
        intent.putExtra(EXTRA_ICON_PATH,categoryPath);
        setResult(RESULT_OK,intent);
        finish();
    }
}
