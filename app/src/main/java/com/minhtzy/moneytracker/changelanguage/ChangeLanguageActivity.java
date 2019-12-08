package com.minhtzy.moneytracker.changelanguage;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.utilities.LanguageUtils;
import com.minhtzy.moneytracker.view.ItemClickListener;
import com.minhtzy.moneytracker.databinding.ActivityChangeLanguageBinding;
import com.minhtzy.moneytracker.model.Language;

public class ChangeLanguageActivity extends AppCompatActivity {

    private LanguageAdapter mLanguageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityChangeLanguageBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_change_language);
        mLanguageAdapter = new LanguageAdapter(LanguageUtils.getLanguageData());
        mLanguageAdapter.setListener(new ItemClickListener<Language>() {
            @Override
            public void onClickItem(int position, Language language) {
                if (!language.getCode().equals(LanguageUtils.getCurrentLanguage().getCode())) {
                    onChangeLanguageSuccessfully(language);
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(ChangeLanguageActivity.this);
        binding.recyclerViewLanguage.setLayoutManager(layoutManager);
        binding.recyclerViewLanguage.setAdapter(mLanguageAdapter);
    }

    public void onChangeLanguageSuccessfully(final Language language) {
        mLanguageAdapter.setCurrentLanguage(language);
        LanguageUtils.changeLanguage(language);
        setResult(RESULT_OK, new Intent());
        finish();
    }
}