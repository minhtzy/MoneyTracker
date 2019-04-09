package com.example.t2m.moneytracker.wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toolbar;

import com.example.t2m.moneytracker.R;

public class AddWalletActivity extends AppCompatActivity {

    Button btnCancel,btnSave,btnNhom;
    EditText txtTen,txtSotien;
    Switch swtTinhtong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wallet);

        addControls();
        addEvents();
    }

    private void addEvents() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnNhom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddWalletActivity.this,SelectCategoryActivity.class);
                startActivity(intent);
            }
        });

    }

    private void addControls() {
        // add custom action bar
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater =(LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.add_wallet_custom_action_bar,null);
        getSupportActionBar().setCustomView(view,new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));

        //
        txtTen = (EditText)findViewById(R.id.txtTen);
        txtSotien = (EditText)findViewById(R.id.txtSotien);
        btnNhom = (Button)findViewById(R.id.btnNhom);
        swtTinhtong = (Switch)findViewById(R.id.swtTichtong);
        btnCancel = (Button)view.findViewById(R.id.btnCancel);
        btnSave = (Button)view.findViewById(R.id.btnSave);
    }
}
