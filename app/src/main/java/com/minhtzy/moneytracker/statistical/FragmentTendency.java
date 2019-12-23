package com.minhtzy.moneytracker.statistical;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.minhtzy.moneytracker.MainActivity;
import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.dataaccess.ITransactionsDAO;
import com.minhtzy.moneytracker.dataaccess.TransactionsDAOImpl;
import com.minhtzy.moneytracker.entity.TransactionEntity;
import com.minhtzy.moneytracker.entity.WalletEntity;
import com.minhtzy.moneytracker.model.DateRange;
import com.minhtzy.moneytracker.model.MTDate;
import com.minhtzy.moneytracker.transaction.TransactionFragment;
import com.minhtzy.moneytracker.utilities.DateUtils;
import com.minhtzy.moneytracker.utilities.ResourceUtils;
import com.minhtzy.moneytracker.utilities.WalletsManager;
import com.minhtzy.moneytracker.wallet.adapter.WalletListAdapter;

import java.util.ArrayList;
import java.util.List;


public class FragmentTendency extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private Spinner mSpinnerItems;

    private Spinner mSpinnerSelect;

    private RelativeLayout mTimeStart;

    private RelativeLayout mTimeEnd;

    private Spinner mSpinnerTimeStart;

    private Spinner mSpinnerTimeEnd;

    private int mSpItemsClick;

    private int mSpSelectClick;

    private List<TransactionEntity> mListTransaction = new ArrayList<>();

    private MTDate mDateStart;

    private MTDate mDateEnd;


    private TextView mTextFromDate;

    private TextView mTextEndDate;

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
    private TextView mTextWallet;
    private ImageView mImgWallet;
    private List<WalletEntity> mListWallet;
    WalletEntity mCurrentWallet;

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
        mTimeStart = view.findViewById(R.id.layout_time_start);
        mTimeEnd = view.findViewById(R.id.layout_time_end);

        mTextFromDate = view.findViewById(R.id.text_time_start);
        mTextEndDate = view.findViewById(R.id.text_time_end);
        mTextWallet = view.findViewById(R.id.text_transaction_wallet);
        mImgWallet = view.findViewById(R.id.image_transaction_wallet);

        ArrayAdapter<CharSequence> adapterItems = ArrayAdapter.createFromResource(view.getContext(), R.array.spinnerItems, android.R.layout.simple_spinner_item);
        adapterItems.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerItems.setAdapter(adapterItems);

        ArrayAdapter<CharSequence> adapterSelect = ArrayAdapter.createFromResource(view.getContext(), R.array.spinnerSelect, android.R.layout.simple_spinner_item);
        adapterSelect.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerSelect.setAdapter(adapterSelect);

        initData();

        initEvents(view);

        return view;
    }

    private void onSelectWallet() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this.getContext());
        builderSingle.setIcon(R.drawable.ic_account_balance_wallet_black_24dp);
        builderSingle.setTitle("Chọn ví của bạn");

        mListWallet = WalletsManager.getInstance(this.getContext()).getAllWallet();
        final ArrayAdapter arrayAdapter = new WalletListAdapter(this.getActivity(), R.layout.custom_item_wallet,mListWallet);

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                WalletEntity selectWallet = mListWallet.get(which);
                if(mCurrentWallet.getWalletId().equals(selectWallet.getWalletId()));
                mCurrentWallet = selectWallet;
                updateWalletUI();
                notifyDataChange();
                dialog.dismiss();
            }
        });
        builderSingle.show();
    }

    private void updateWalletUI() {
        mTextWallet.setText(mCurrentWallet.getName());
        mImgWallet.setImageDrawable(ResourceUtils.getWalletIcon(mCurrentWallet.getIcon()));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initData() {

        DateRange monthRange = new DateUtils().getDateRangeForPeriod(getContext(),R.string.current_month);
        mDateStart = monthRange.getDateFrom();
        mDateEnd = monthRange.getDateTo();

        ITransactionsDAO iTransactionsDAO = new TransactionsDAOImpl(getContext());
        mCurrentWallet = WalletsManager.getInstance(getContext()).getCurrentWallet();
        updateWalletUI();
        mListTransaction = iTransactionsDAO.getAllTransactionByPeriod(mCurrentWallet.getWalletId(),monthRange);

        mTextFromDate.setText(mDateStart.toIsoDateString());

        mTextEndDate.setText(mDateEnd.toIsoDateString());
    }

    private void initEvents(View view) {

        mSpinnerItems.setOnItemSelectedListener(mSpinnerItemsListener);
        mSpinnerSelect.setOnItemSelectedListener(mSpinnerSelectedListener);

        view.findViewById(R.id.layout_wallet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectWallet();
            }
        });

        view.findViewById(R.id.layout_time_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTimeStart(v);
            }
        });

        view.findViewById(R.id.layout_time_end).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTimeEnd(v);
            }
        });
    }

    private void onClickTimeStart(View v) {

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if(mDateStart.getYear() == year && mDateStart.getMonth() == month && mDateStart.getDayOfMonth() == dayOfMonth) return;
                mDateStart.setYear(year);
                mDateStart.setMonth(month);
                mDateStart.setDate(dayOfMonth);
                mTextFromDate.setText(mDateStart.toIsoDateString());
                notifyDataChange();
            }
        };
        new DatePickerDialog(v.getContext(), dateSetListener, mDateStart.getYear(), mDateStart.getMonth(), mDateStart.getDayOfMonth()).show();
    }

    private void onClickTimeEnd(View v) {

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if(mDateEnd.getYear() == year && mDateEnd.getMonth() == month && mDateEnd.getDayOfMonth() == dayOfMonth) return;
                mDateEnd.setYear(year);
                mDateEnd.setMonth(month);
                mDateEnd.setDate(dayOfMonth);
                mTextEndDate.setText(mDateEnd.toIsoDateString());
                notifyDataChange();
            }
        };
        new DatePickerDialog(v.getContext(), dateSetListener, mDateEnd.getYear(), mDateEnd.getMonth(), mDateEnd.getDayOfMonth()).show();
    }

    private void notifyDataChange() {
        ITransactionsDAO iTransactionsDAO = new TransactionsDAOImpl(getContext());
        mListTransaction = iTransactionsDAO.getAllTransactionByPeriod(mCurrentWallet.getWalletId(),new DateRange(mDateStart,mDateEnd));
        handleItemClick();
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
