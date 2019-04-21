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

import com.example.t2m.moneytracker.MainActivity;
import com.example.t2m.moneytracker.R;
import com.example.t2m.moneytracker.dataaccess.IWalletsDAO;
import com.example.t2m.moneytracker.dataaccess.MoneyTrackerDBHelper;
import com.example.t2m.moneytracker.dataaccess.WalletsDAOImpl;
import com.example.t2m.moneytracker.model.Wallet;
import com.google.firebase.auth.FirebaseAuth;

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
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWallet();
                Intent intent = new Intent(AddWalletActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void addWallet() {
        String name = txtTen.getText().toString();
        int soTien = Integer.parseInt(txtSotien.getText().toString());
        if(name.isEmpty() ) {
            txtTen.setError("Tên không được để trống");
            txtTen.requestFocus();
            return;
        }
        IWalletsDAO iWalletsDAO = new WalletsDAOImpl(this);
        Wallet wallet = new Wallet.WalletBuilder()
                .setWalletName(name)
                .setCurrentBalance(soTien)
                .setUserUID(FirebaseAuth.getInstance().getUid())
                .setCurrencyCode("VND")
                .setWalletType(1)
                .setImageSrc("")
                .build();
        iWalletsDAO.insertWallet(wallet);
    }

    private void addControls() {
        // add custom action bar
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater =(LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_action_bar_add_wallet,null);
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
