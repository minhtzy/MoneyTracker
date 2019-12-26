package com.minhtzy.moneytracker.view.setting;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.minhtzy.moneytracker.MainActivity;
import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.view.account.LoginActivity;
import com.minhtzy.moneytracker.view.changelanguage.ChangeLanguageActivity;
import com.minhtzy.moneytracker.model.Constants;
import com.minhtzy.moneytracker.databinding.ActivitySettingBinding;
import com.minhtzy.moneytracker.utilities.LanguageUtils;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Setting extends AppCompatActivity {
    private FirebaseAuth mAtuth;
    FirebaseUser user;
    TextView txtUsername;
    Button btnLanguage,btnLogout,btnBack;
    private ActivitySettingBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        FirebaseApp.initializeApp(Setting.this);
        mAtuth = FirebaseAuth.getInstance();

        LanguageUtils.loadLocale();
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        mBinding.setMain(Setting.this);


        addControls();
        addEvents();
    }

    private void addControls() {
        txtUsername = (TextView) findViewById(R.id.txtUsername);
        btnLanguage = (Button) findViewById(R.id.btnLanguage);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnBack = (Button)findViewById(R.id.btnback);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            txtUsername.setText(user.getEmail());
        }
    }

    private void addEvents() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogoutAccount();
            }

            private void LogoutAccount() {
                mAtuth.signOut();
                Intent intent = new Intent(Setting.this, LoginActivity.class);
                startActivity(intent);

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.RequestCode.CHANGE_LANGUAGE:
                if (resultCode == RESULT_OK) {
                    updateViewByLanguage();
                }
                break;
        }
    }
    private void updateViewByLanguage() {

        recreate();
        //        mBinding.btnLanguage.setText(getString(R.string.change_language));
//        mBinding.btnLogout.setText(getString(R.string.signout));
//        mBinding.btnback.setText(getString(R.string.back));
//        mBinding.textView8.setText(getString(R.string.name_account));
//        mBinding.textview09.setText(getString(R.string.action_settings));

    }
    public void openLanguageScreen() {
        Intent intent = new Intent(Setting.this, ChangeLanguageActivity.class);
        startActivityForResult(intent, Constants.RequestCode.CHANGE_LANGUAGE);
    }

}