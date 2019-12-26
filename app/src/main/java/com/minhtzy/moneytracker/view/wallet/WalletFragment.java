package com.minhtzy.moneytracker.view.wallet;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.view.wallet.adapter.WalletRecycleViewAdapter;
import com.minhtzy.moneytracker.entity.WalletEntity;
import com.minhtzy.moneytracker.utilities.WalletsManager;

import org.parceler.Parcels;

import java.util.List;

public class WalletFragment extends Fragment implements OnWalletItemInteractionListener {

    private Button mBtnAddWallet;
    RecyclerView mListWallet;
    List<WalletEntity> walletEntityList;
    WalletRecycleViewAdapter adapter;
    public WalletFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_wallet, container, false);

        mBtnAddWallet = view.findViewById(R.id.btn_add_wallet);
        mListWallet = view.findViewById(R.id.list_wallet);

        addEvents();
        return view;
    }

    private void addEvents() {
        loadWallet();
        mBtnAddWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddWallet();
            }
        });
    }

    private void loadWallet() {
        walletEntityList = WalletsManager.getInstance(this.getContext()).getAllWallet();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        mListWallet.setLayoutManager(layoutManager);

        adapter = new WalletRecycleViewAdapter(walletEntityList,this);
        mListWallet.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        mListWallet.addItemDecoration(dividerItemDecoration);
    }

    private void onAddWallet() {
        Intent intent = new Intent(WalletFragment.this.getContext(),SelectWalletTypeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onWalletItemClicked(WalletEntity entity) {
        Intent intent = new Intent(this.getContext(),DetailWalletActivity.class);
        intent.putExtra(DetailWalletActivity.EXTRA_WALLET_DETAIL, Parcels.wrap(entity));
        startActivityForResult(intent,10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 10)
        {
            walletEntityList.clear();
            walletEntityList.addAll(WalletsManager.getInstance(this.getContext()).getAllWallet());
            adapter.notifyDataSetChanged();
        }
    }
}
