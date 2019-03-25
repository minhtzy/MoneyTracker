package com.example.t2m.moneytracker.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.example.t2m.moneytracker.R;

public class Addwallet extends AppCompatActivity {

    Button btnCancel,btnSave,btnNhom;
    EditText txtTen,txtSotien;
    Switch swtTinhtong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addwallet);

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
                Intent intent = new Intent(Addwallet.this,SelectCategoryActivity.class);
                startActivity(intent);
            }
        });

    }

    private void addControls() {
        txtTen = (EditText)findViewById(R.id.txtTen);
        txtSotien = (EditText)findViewById(R.id.txtSotien);
        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnSave = (Button)findViewById(R.id.btnSave);
        btnNhom = (Button)findViewById(R.id.btnNhom);
        swtTinhtong = (Switch)findViewById(R.id.swtTichtong);
    }
}
