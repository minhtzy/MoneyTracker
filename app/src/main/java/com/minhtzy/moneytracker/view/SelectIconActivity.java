package com.minhtzy.moneytracker.view;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.view.adapter.CategoryIconListAdapter;

public class SelectIconActivity extends AppCompatActivity implements OnIconInteractionListener {

    public static final String EXTRA_ICON_PATH = "CategoryIcon.path" ;
    public static final String EXTRA_FOLDER = "Icon.folder";
    RecyclerView mListView;
    String mFolder = "category";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addControls();
        addEvents();
    }

    private void addControls() {

        if(getIntent().hasExtra(EXTRA_FOLDER))
        {
            mFolder = getIntent().getStringExtra(EXTRA_FOLDER);
        }

        mListView = findViewById(R.id.list_cat_icon);
        mListView.setLayoutManager(new GridLayoutManager(this,5));
        mListView.setAdapter(new CategoryIconListAdapter(this,this,mFolder));
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
