package com.minhtzy.moneytracker.view.statistical;


import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;

import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.dataaccess.CategoriesDAOImpl;
import com.minhtzy.moneytracker.dataaccess.ICategoriesDAO;
import com.minhtzy.moneytracker.entity.CategoryEntity;
import com.minhtzy.moneytracker.entity.TransactionEntity;
import com.minhtzy.moneytracker.view.statistical.adapter.ListTransactionPBAdapter;
import com.minhtzy.moneytracker.utilities.ResourceUtils;
import com.minhtzy.moneytracker.view.CurrencyTextView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;


@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class FragmentPagerPB extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "FragmentPagerPB";

    private static final String ARG_ITEMS = "items";

    private static final String ARG_NUMBER = "numberID";

    // TODO: Rename and change types of parameters

    private List<TransactionEntity> mItems = new ArrayList<>();

    private int mNumberID;

    private TextView mTextNotePBDebt;

    private TextView mTextNotePBLoan;

    private CurrencyTextView mTradingMoney;

    private CardView mCardView;

    private ImageView mImgCategory;

    private TextView mNumberTransaction;

    private CurrencyTextView mMoneyTotal;

    private RecyclerView mRecyclerViewOV;

    private final View.OnClickListener mCardViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            itemOnClick();
        }
    };

    public FragmentPagerPB() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentPagerPB newInstance(List<TransactionEntity> items, int numberID) {
        FragmentPagerPB fragment = new FragmentPagerPB();
        Bundle args = new Bundle();
        if (items != null) {

            args.putParcelable(ARG_ITEMS, Parcels.wrap(items));
            args.putInt(ARG_NUMBER, numberID);

        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            mItems.addAll((List<TransactionEntity>)Parcels.unwrap(getArguments().getParcelable(ARG_ITEMS)));
            mNumberID = getArguments().getInt(ARG_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pager_paybook, container, false);
        mTextNotePBDebt = view.findViewById(R.id.txtTextNoteDebt);
        mTextNotePBLoan = view.findViewById(R.id.txtTextNoteLoan);
        mTradingMoney = view.findViewById(R.id.txtTradingPB);
        mCardView = view.findViewById(R.id.card_view1);
        mImgCategory = view.findViewById(R.id.imgCategory);
        mNumberTransaction = view.findViewById(R.id.txtNumberTransaction);
        mMoneyTotal = view.findViewById(R.id.txtMoneyTotal);
        mRecyclerViewOV = view.findViewById(R.id.recycleViewOV);

        initEvents();


        return view;
    }

    private void initEvents() {

        mCardView.setOnClickListener(mCardViewListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void itemOnClick() {


    }

    @Override
    public void onResume() {
        super.onResume();
        onBindView();
    }

    @SuppressLint({"SetTextI18n", "WrongConstant"})
    private void onBindView() {

        if (mNumberID == 59) {

            mTextNotePBLoan.setVisibility(View.GONE);

            float moneyDebt = 0;
            int count = 0;
            for (TransactionEntity tran : mItems) {

                moneyDebt += Math.abs(tran.getTransactionAmount());
                count++;
            }
            mTradingMoney.setText(String.valueOf(moneyDebt));
            mNumberTransaction.setText(String.valueOf(count));
            mMoneyTotal.setText(String.valueOf(moneyDebt));

            ICategoriesDAO iCategoriesDAO = new CategoriesDAOImpl(getContext());
            CategoryEntity category = iCategoriesDAO.getCategoryById(59);
            String icon = category.getCategoryIcon();

            mImgCategory.setImageDrawable(ResourceUtils.getCategoryIcon(icon));


        } else if (mNumberID == 57) {

            mTextNotePBDebt.setVisibility(View.GONE);
            float moneyLoan = 0;
            int count = 0;
            for (TransactionEntity tran : mItems) {

                moneyLoan += Math.abs(tran.getTransactionAmount());
                count++;
            }
            mTradingMoney.setText(String.valueOf(moneyLoan));
            mNumberTransaction.setText(String.valueOf(count));
            mMoneyTotal.setText(String.valueOf(moneyLoan));

            ICategoriesDAO iCategoriesDAO = new CategoriesDAOImpl(getContext());
            CategoryEntity category = iCategoriesDAO.getCategoryById(57);
            String icon = category.getCategoryIcon();

            mImgCategory.setImageDrawable(ResourceUtils.getCategoryIcon(category.getCategoryIcon()));

        }

        mRecyclerViewOV.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.canScrollVertically();
        ListTransactionPBAdapter adapter = new ListTransactionPBAdapter(mItems);
        mRecyclerViewOV.setLayoutManager(llm);
        mRecyclerViewOV.setAdapter(adapter);
    }
}
