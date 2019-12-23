package com.minhtzy.moneytracker.sync;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.minhtzy.moneytracker.MainActivity;
import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.dataaccess.IWalletsDAO;
import com.minhtzy.moneytracker.dataaccess.WalletsDAOImpl;
import com.minhtzy.moneytracker.utilities.WalletsManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.minhtzy.moneytracker.wallet.SelectWalletTypeActivity;

public class SyncActivity extends AppCompatActivity implements SyncEvents {

    TextView textOnSync;
    TextView textTryAgain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
        addControls();
        addEvents();
    }

    private void addControls() {
        textOnSync = findViewById(R.id.loading_text);
        textTryAgain = findViewById(R.id.btnTryAgain);
    }

    private void addEvents() {

        textTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSync();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        onSync();
    }

    private void onSync() {
        textTryAgain.setVisibility(View.INVISIBLE);
        textOnSync.setVisibility(View.VISIBLE);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        SyncCloudFirestore syncCloudFirestore = new SyncCloudFirestore(this);
        syncCloudFirestore.setSyncEvents(this);
        syncCloudFirestore.onPullWallet(user.getUid());
    }

    @Override
    public void onPullWalletComplete() {
        IWalletsDAO iWalletsDAO = new WalletsDAOImpl(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(iWalletsDAO.hasWallet(user.getUid()) ) {
            SyncCloudFirestore syncCloudFirestore = new SyncCloudFirestore(this);
            syncCloudFirestore.setSyncEvents(this);
            syncCloudFirestore.onPullTransactions(user.getUid());
        }
        else {
            Intent intent = new Intent(SyncActivity.this, SelectWalletTypeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onPullWalletFailure() {
        textTryAgain.setVisibility(View.VISIBLE);
        textOnSync.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPullTransactionComplete() {
        Intent intent = new Intent(SyncActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPullTransactionFailure() {
        textTryAgain.setVisibility(View.VISIBLE);
        textOnSync.setVisibility(View.INVISIBLE);
    }
}
