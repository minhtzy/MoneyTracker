package com.minhtzy.moneytracker.statistical;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.dataaccess.ITransactionsDAO;
import com.minhtzy.moneytracker.dataaccess.TransactionsDAOImpl;
import com.minhtzy.moneytracker.entity.TransactionEntity;
import com.minhtzy.moneytracker.entity.WalletEntity;
import com.minhtzy.moneytracker.model.DateRange;
import com.minhtzy.moneytracker.model.MTDate;
import com.minhtzy.moneytracker.utilities.DateUtils;
import com.minhtzy.moneytracker.utilities.WalletsManager;

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

        ArrayAdapter<CharSequence> adapterItems = ArrayAdapter.createFromResource(view.getContext(), R.array.spinnerItems, android.R.layout.simple_spinner_item);
        adapterItems.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerItems.setAdapter(adapterItems);

        ArrayAdapter<CharSequence> adapterSelect = ArrayAdapter.createFromResource(view.getContext(), R.array.spinnerSelect, android.R.layout.simple_spinner_item);
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

        DateRange monthRange = new DateUtils().getDateRangeForPeriod(getContext(),R.string.current_month);
        mDateStart = monthRange.getDateFrom();
        mDateEnd = monthRange.getDateTo();

        ITransactionsDAO iTransactionsDAO = new TransactionsDAOImpl(getContext());
        WalletEntity wallet = WalletsManager.getInstance(getContext()).getCurrentWallet();
        mListTransaction = iTransactionsDAO.getAllTransactionByPeriod(wallet.getWalletId(),monthRange);

        mTextFromDate.setText(mDateStart.toIsoDateString());

        mTextEndDate.setText(mDateEnd.toIsoDateString());
    }

    private void initEvents() {

        mSpinnerItems.setOnItemSelectedListener(mSpinnerItemsListener);
        mSpinnerSelect.setOnItemSelectedListener(mSpinnerSelectedListener);

        mTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTimeStart(v);
            }
        });

        mTimeEnd.setOnClickListener(new View.OnClickListener() {
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
                notifyDateChange();
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
                notifyDateChange();
            }
        };
        new DatePickerDialog(v.getContext(), dateSetListener, mDateEnd.getYear(), mDateEnd.getMonth(), mDateEnd.getDayOfMonth()).show();
    }

    private void notifyDateChange() {
        ITransactionsDAO iTransactionsDAO = new TransactionsDAOImpl(getContext());
        WalletEntity wallet = WalletsManager.getInstance(getContext()).getCurrentWallet();
        mListTransaction = iTransactionsDAO.getAllTransactionByPeriod(wallet.getWalletId(),new DateRange(mDateStart,mDateEnd));
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
