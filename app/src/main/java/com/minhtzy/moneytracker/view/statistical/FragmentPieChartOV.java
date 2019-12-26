package com.minhtzy.moneytracker.view.statistical;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Pair;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.dataaccess.CategoriesDAOImpl;
import com.minhtzy.moneytracker.dataaccess.ICategoriesDAO;
import com.minhtzy.moneytracker.entity.CategoryEntity;
import com.minhtzy.moneytracker.entity.TransactionEntity;
import com.minhtzy.moneytracker.model.TransactionTypes;
import com.minhtzy.moneytracker.view.statistical.adapter.ChartOVTransactionAdapter;
import com.minhtzy.moneytracker.utilities.CategoryManager;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class FragmentPieChartOV extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ITEMS = "items";

    private static final String ARG_POSITION = "position";

    // TODO: Rename and change types of parameters
    private PieChart mPieChart;

    private RecyclerView mRecycleViewOV;

    private List<TransactionEntity> mListTransactions = new ArrayList<>();

    private int mPosition;

    private final OnChartValueSelectedListener mPiechartListener = new OnChartValueSelectedListener() {
        @Override
        public void onValueSelected(Entry e, Highlight h) {

//            showItemSelect(e, h, mYData, mXData);

        }

        @Override
        public void onNothingSelected() {

        }
    };


    public FragmentPieChartOV() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters

    public static Fragment newInstance(List<TransactionEntity> listTransaction, int position) {
        FragmentPieChartOV fragment = new FragmentPieChartOV();
        Bundle args = new Bundle();
        if (listTransaction != null) {

            args.putParcelable(ARG_ITEMS, Parcels.wrap(listTransaction));
            args.putInt(ARG_POSITION, position);
        }

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            mListTransactions.addAll((List<TransactionEntity>) Parcels.unwrap(getArguments().getParcelable(ARG_ITEMS)));
            mPosition = getArguments().getInt(ARG_POSITION);
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chart_ov_transactions, container, false);

        mPieChart = view.findViewById(R.id.piechartItems);

        mPieChart.setRotationEnabled(true);
        mPieChart.getDescription().setText("Biểu đồ thống kê giao dịch");
        mPieChart.setHoleRadius(35f);
        mPieChart.setTransparentCircleAlpha(0);
        mPieChart.setCenterText("");
        mPieChart.setCenterTextSize(10);
        mPieChart.setDrawEntryLabels(true);

        mPieChart.setDrawSliceText(false); // To remove slice text
        mPieChart.setDrawMarkers(false); // To remove markers when click
        mPieChart.setDrawEntryLabels(false); // To remove labels from piece of pie
        mPieChart.getDescription().setEnabled(false);

        mRecycleViewOV = view.findViewById(R.id.recycleViewOV);
        mRecycleViewOV.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        mRecycleViewOV.setLayoutManager(llm);

        onBindView();

        intitEvents();

        return view;
    }

    private void intitEvents() {

        mPieChart.setOnChartValueSelectedListener(mPiechartListener);
    }

    private void onBindView() {

        float moneyEx = 0, moneyIn = 0;

        List<Pair<Float, String>> listItems = new ArrayList<>();
        List<Pair<Float, String>> totalItems = new ArrayList<>();
        SparseArray<Float> listSumAmount = new SparseArray<>();
        List<Pair<Float, String>> totalItemsEx = new ArrayList<>();
        List<Pair<Float, String>> totalItemsIn = new ArrayList<>();

        if (mListTransactions != null) {

            for (TransactionEntity tran : mListTransactions) {
                int parentID = tran.getCategoryId();
                CategoryEntity categoryEntity = CategoryManager.getInstance().getCategoryById(parentID);
                try
                {
                    parentID = categoryEntity.getParentId();
                }
                catch (NullPointerException ex)
                {

                }
                float value = listSumAmount.get(parentID) != null ?  listSumAmount.get(parentID) : 0;
                float new_value = value + (float) Math.abs(tran.getTransactionAmount());

                listSumAmount.put(parentID, new_value);

                if (tran.getTransactionAmount() < 0) {

                    moneyEx += Math.abs(tran.getTransactionAmount());
                } else {

                    moneyIn += Math.abs(tran.getTransactionAmount());
                }
            }

            if (mPosition == 2) {

                mPieChart.setCenterText("Tất cả");

                listItems.add(new Pair<>(moneyEx, "Khoản chi"));
                listItems.add(new Pair<>(moneyIn, "Khoản thu"));

                for (int i = 0; i < listItems.size(); i++) {

                    if (listItems.get(i).first > 0) {
                        totalItems.add(new Pair<>(listItems.get(i).first, listItems.get(i).second));
                    }
                }

                addDataSet(mPieChart, totalItems);
            }


            ICategoriesDAO categoriesDAO = new CategoriesDAOImpl(getContext());


            if (mPosition == 0) {

                mPieChart.setCenterText("Khoản chi");

                List<CategoryEntity> listCateExpress = categoriesDAO.getCategoriesByType(TransactionTypes.EXPENSE.getValue());
                listCateExpress.addAll(categoriesDAO.getCategoriesByType(TransactionTypes.LOAN.getValue()));

                for (CategoryEntity categoryEntity : listCateExpress) {

                    if (listSumAmount.indexOfKey(categoryEntity.getCategoryId()) >= 0 && listSumAmount.get(categoryEntity.getCategoryId()) > 0) {

                        totalItemsEx.add(new Pair<>(listSumAmount.get(categoryEntity.getCategoryId()), categoryEntity.getCategoryName()));
                    }
                }
                addDataSet(mPieChart, totalItemsEx);
                ChartOVTransactionAdapter transactionAdapter = new ChartOVTransactionAdapter(totalItemsEx);
                mRecycleViewOV.setAdapter(transactionAdapter);
            }

            if (mPosition == 1) {

                mPieChart.setCenterText("Khoản Thu");

                List<CategoryEntity> listCateIncome = categoriesDAO.getCategoriesByType(TransactionTypes.INCOME.getValue());
                listCateIncome.addAll(categoriesDAO.getCategoriesByType(TransactionTypes.DEBIT.getValue()));

                for (CategoryEntity categoryEntity : listCateIncome) {

                    if (listSumAmount.indexOfKey(categoryEntity.getCategoryId()) >= 0 && listSumAmount.get(categoryEntity.getCategoryId()) > 0)
                        totalItemsIn.add(new Pair<>(listSumAmount.get(categoryEntity.getCategoryId()), categoryEntity.getCategoryName()));
                }

                addDataSet(mPieChart, totalItemsIn);
                ChartOVTransactionAdapter transactionAdapter = new ChartOVTransactionAdapter(totalItemsIn);
                mRecycleViewOV.setAdapter(transactionAdapter);
            }
        }
    }

    private void addDataSet(PieChart pieChart, List<Pair<Float, String>> totalData) {
        ArrayList<PieEntry> yEntries = new ArrayList<>();
        ArrayList<String> xEntries = new ArrayList<>();

        for (int i = 0; i < totalData.size(); i++) {

            yEntries.add(new PieEntry(totalData.get(i).first, i));
            xEntries.add(totalData.get(i).second);
        }


        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntries, "TransactionChart");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();

        colors.add(Color.GRAY);
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        colors.add(Color.YELLOW);
        colors.add(Color.MAGENTA);
        colors.add(Color.BLACK);
        colors.add(R.color.o_300);
        colors.add(Color.LTGRAY);
        colors.add(R.color.o_900);
        colors.add(Color.DKGRAY);


        pieDataSet.setColors(colors);

        List<LegendEntry> entries = new ArrayList<>();

        for (int i = 0; i < xEntries.size(); i++) {
            LegendEntry entry = new LegendEntry();
            entry.formColor = colors.get(i);
            entry.label = xEntries.get(i);
            entries.add(entry);
        }
        //add legend to chart
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setCustom(entries);

        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER); // set vertical alignment for legend
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT); // set horizontal alignment for legend
        legend.setOrientation(Legend.LegendOrientation.VERTICAL); // set orientation for legend
        legend.setDrawInside(false);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();

    }

}
