package com.minhtzy.moneytracker.statistical;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.dataaccess.ITransactionsDAO;
import com.minhtzy.moneytracker.dataaccess.TransactionsDAOImpl;
import com.minhtzy.moneytracker.entity.TransactionEntity;
import com.minhtzy.moneytracker.entity.WalletEntity;
import com.minhtzy.moneytracker.utilities.WalletsManager;

import java.util.ArrayList;
import java.util.List;


public class FragmentTendency extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private Spinner mSpinnerItems;

    private Spinner mSpinnerSelect;

    private int mSpItemsClick;

    private int mSpSelectClick;

    private List<TransactionEntity> mListTransaction = new ArrayList<>();

    private final AdapterView.OnItemSelectedListener mSpinnerItemsListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

            mSpItemsClick = position;
            handleItemClick();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };


    private final AdapterView.OnItemSelectedListener mSpinnerSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

            mSpSelectClick = position;
            handleItemClick();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };


//    private Spinner mTimeStart;
//
//    private Spinner mTimeEnd;

    public FragmentTendency() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tendency, container, false);
        mSpinnerItems = view.findViewById(R.id.spinnerItems);
        mSpinnerSelect = view.findViewById(R.id.spinnerSelect);
//        mTimeStart = view.findViewById(R.id.spinnerTimeStart);
//        mTimeEnd = view.findViewById(R.id.spinnerTimeEnd);

        ArrayAdapter<CharSequence> adapterItems = ArrayAdapter.createFromResource(getContext(), R.array.spinnerItems, android.R.layout.simple_spinner_item);
        adapterItems.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerItems.setAdapter(adapterItems);

        ArrayAdapter<CharSequence> adapterSelect = ArrayAdapter.createFromResource(getContext(), R.array.spinnerSelect, android.R.layout.simple_spinner_item);
        adapterSelect.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerSelect.setAdapter(adapterSelect);

        initData();

        initEvents();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initData() {
        ITransactionsDAO iTransactionsDAO = new TransactionsDAOImpl(getContext());
        WalletEntity wallet = WalletsManager.getInstance(getContext()).getCurrentWallet();
        mListTransaction = iTransactionsDAO.getAllTransactionByWalletId(wallet.getWalletId());
    }

    private void initEvents() {

        mSpinnerItems.setOnItemSelectedListener(mSpinnerItemsListener);
        mSpinnerSelect.setOnItemSelectedListener(mSpinnerSelectedListener);

    }


    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private void handleItemClick() {

        List<TransactionEntity> tranEx = new ArrayList<>();
        List<TransactionEntity> tranIn = new ArrayList<>();
        Fragment fragment;
        FrameLayout frameLayout = getActivity().findViewById(R.id.frame_layout_tendency);

        for (TransactionEntity tran : mListTransaction) {

            if (tran.getTransactionAmount() < 0) {

                tranEx.add(tran);

            } else {

                tranIn.add(tran);
            }
        }

        switch (mSpSelectClick) {

            case 0:

                switch (mSpItemsClick) {

                    case 2:
                        frameLayout.setVisibility(View.VISIBLE);
                        fragment = FragmentPieChartOV.newInstance(mListTransaction, 2);
                        assert fragment != null;
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_tendency, fragment).commit();

                        break;

                    case 0:
                        frameLayout.setVisibility(View.VISIBLE);
                        fragment = FragmentPieChartOV.newInstance(tranEx, 0);
                        assert fragment != null;
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_tendency, fragment).commit();

                        break;

                    case 1:
                        frameLayout.setVisibility(View.VISIBLE);
                        fragment = FragmentPieChartOV.newInstance(tranIn, 1);
                        assert fragment != null;
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_tendency, fragment).commit();

                        break;

                }

                break;

            case 1:

                switch (mSpItemsClick) {

                    case 2:
                        Toast.makeText(getContext(), getString(R.string.feature_developing), Toast.LENGTH_SHORT).show();

                        frameLayout.setVisibility(View.INVISIBLE);
//                        fragment = FragmentBarChartOv.newInstance(mListTransaction, 2);
//                        assert fragment != null;
//                        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_tendency, fragment).commit();
//
                        break;

                    case 0:
                        frameLayout.setVisibility(View.VISIBLE);
                        fragment = FragmentBarChartOv.newInstance(tranEx, 0);
                        assert fragment != null;
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_tendency, fragment).commit();

                        break;

                    case 1:
                        frameLayout.setVisibility(View.VISIBLE);
                        fragment = FragmentBarChartOv.newInstance(tranIn, 1);
                        assert fragment != null;
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_tendency, fragment).commit();

                        break;

                }

                break;


        }

    }

}
