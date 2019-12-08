package com.minhtzy.moneytracker.statistical;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.dataaccess.ITransactionsDAO;
import com.minhtzy.moneytracker.dataaccess.IWalletsDAO;
import com.minhtzy.moneytracker.dataaccess.TransactionsDAOImpl;
import com.minhtzy.moneytracker.dataaccess.WalletsDAOImpl;
import com.minhtzy.moneytracker.entity.TransactionEntity;
import com.minhtzy.moneytracker.entity.WalletEntity;
import com.minhtzy.moneytracker.statistical.adapter.PayBookPagerAdapter;

import java.util.List;

public class FragmentPayBook extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "FragmentPayBook";

    private ITransactionsDAO mITransactionsDAO ;


    public FragmentPayBook() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pay_book, container, false);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        ViewPager viewPager = view.findViewById(R.id.viewPager);

        IWalletsDAO iWalletsDAO = new WalletsDAOImpl(getContext());
        ITransactionsDAO iTransactionsDAO = new TransactionsDAOImpl(getContext());
        WalletEntity wallet = iWalletsDAO.getAllWalletByUser(FirebaseAuth.getInstance().getCurrentUser().getUid()).get(0);
        List<TransactionEntity> mItems = iTransactionsDAO.getAllTransactionByWalletId(wallet.getWalletId());

        PayBookPagerAdapter adapter = new PayBookPagerAdapter(getChildFragmentManager(), mItems);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.pay_book);
    }
}
