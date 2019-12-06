package com.minhtzy.moneytracker.statistical;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
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
import com.minhtzy.moneytracker.entity.TransactionEntity;
import com.minhtzy.moneytracker.statistical.adapter.ChartOVTransactionAdapter;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        if (listTransaction != null ) {

            args.putParcelableArrayList(ARG_ITEMS, (ArrayList<? extends Parcelable>) listTransaction);
            args.putInt(ARG_POSITION, position);
        }

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            mListTransactions.addAll((List<TransactionEntity>)Parcels.unwrap(getArguments().getParcelable(ARG_ITEMS)));
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

        float money1 = 0, money5 = 0, money13 = 0, money18 = 0,
                money23 = 0, money24 = 0, money27 = 0, money28 = 0,
                money33 = 0, money37 = 0, money42 = 0, money49 = 0, money51 = 0,
                money53 = 0, money56 = 0;
        float moneyEx = 0, moneyIn = 0;

        List<Pair<Float, String>> listItems = new ArrayList<>();
        List<Pair<Float, String>> totalItems = new ArrayList<>();
        List<Pair<Float, String>> listItemsEx = new ArrayList<>();
        List<Pair<Float, String>> totalItemsEx = new ArrayList<>();
        List<Pair<Float, String>> listItemsIn = new ArrayList<>();
        List<Pair<Float, String>> totalItemsIn = new ArrayList<>();

        if (mListTransactions != null) {

            if (mPosition == 2) {

                mPieChart.setCenterText("Tất cả");

                for (TransactionEntity tran : mListTransactions) {

                    if (tran.getTransactionAmount() < 0) {

                        moneyEx += Math.abs(tran.getTransactionAmount());
                    } else {

                        moneyIn += Math.abs(tran.getTransactionAmount());
                    }
                }

                listItems.add(new Pair<>(moneyEx, "Khoản chi"));
                listItems.add(new Pair<>(moneyIn, "Khoản thu"));

                for (int i = 0; i < listItems.size(); i++) {

                    if (listItems.get(i).first > 0) {

                        totalItems.add(new Pair<>(listItems.get(i).first, listItems.get(i).second));
//                        mYData.add(listItems.get(i).first);
//                        mXData.add(listItems.get(i).second);
                    }
                }

                addDataSet(mPieChart, totalItems);
            }

            if (mPosition == 0) {

                mPieChart.setCenterText("Khoản chi");

                for (TransactionEntity tran : mListTransactions) {

                    if (tran.getCategoryId() == 1) {

                        money1 += Math.abs(tran.getTransactionAmount());
                    }

                    if (tran.getTransactionCategoryID().getCategoryParentId() == 5) {

                        money5 += Math.abs(tran.getMoneyTradingWithSign());
                    }

                    if (tran.getTransactionCategoryID().getCategoryParentId() == 13) {

                        money13 += Math.abs(tran.getMoneyTradingWithSign());
                    }

                    if (tran.getTransactionCategoryID().getCategoryParentId() == 18) {

                        money18 += Math.abs(tran.getMoneyTradingWithSign());
                    }

                    if (tran.getTransactionCategoryID().getCategoryParentId() == 23) {

                        money23 += Math.abs(tran.getMoneyTradingWithSign());
                    }

                    if (tran.getTransactionCategoryID().getCategoryParentId() == 24) {

                        money24 += Math.abs(tran.getMoneyTradingWithSign());
                    }

                    if (tran.getTransactionCategoryID().getCategoryParentId() == 27) {

                        money27 += Math.abs(tran.getMoneyTradingWithSign());
                    }

                    if (tran.getTransactionCategoryID().getCategoryParentId() == 28) {

                        money28 += Math.abs(tran.getMoneyTradingWithSign());
                    }

                    if (tran.getTransactionCategoryID().getCategoryParentId() == 33) {

                        money33 += Math.abs(tran.getMoneyTradingWithSign());
                    }

                    if (tran.getTransactionCategoryID().getCategoryParentId() == 37) {

                        money37 += Math.abs(tran.getMoneyTradingWithSign());
                    }

                    if (tran.getTransactionCategoryID().getCategoryParentId() == 42) {

                        money42 += Math.abs(tran.getMoneyTradingWithSign());
                    }

                    if (tran.getTransactionCategoryID().getCategoryParentId() == 49) {

                        money49 += Math.abs(tran.getMoneyTradingWithSign());
                    }

                }

                listItemsEx.add(new Pair<>(money1, "Ăn uống"));
                listItemsEx.add(new Pair<>(money5, "Hóa đơn & Tiện ích"));
                listItemsEx.add(new Pair<>(money13, "Di chuyển"));
                listItemsEx.add(new Pair<>(money18, "Mua sắm"));
                listItemsEx.add(new Pair<>(money23, "Bạn bè & Người yêu"));
                listItemsEx.add(new Pair<>(money24, "Giải trí"));
                listItemsEx.add(new Pair<>(money27, "Du lịch"));
                listItemsEx.add(new Pair<>(money28, "Sức khỏe"));
                listItemsEx.add(new Pair<>(money33, "Quà tặng & Quyên góp"));
                listItemsEx.add(new Pair<>(money37, "Gia đình"));
                listItemsEx.add(new Pair<>(money42, "Giáo dục"));
                listItemsEx.add(new Pair<>(money49, "Khoản chi khác"));

                for (int i = 0; i < listItemsEx.size(); i++) {

                    if (listItemsEx.get(i).first > 0) {

                        totalItemsEx.add(new Pair<>(listItemsEx.get(i).first, listItemsEx.get(i).second));
//                        mYData.add(listItemsEx.get(i).first);
//                        mXData.add(listItemsEx.get(i).second);
                    }
                }

                addDataSet(mPieChart, totalItemsEx);
                ChartOVTransactionAdapter transactionAdapter = new ChartOVTransactionAdapter(totalItemsEx);
                mRecycleViewOV.setAdapter(transactionAdapter);
            }

            if (mPosition == 1) {

                mPieChart.setCenterText("Khoản Thu");

                for (TransactionEntity tran : mListTransactions) {

                    if (tran.getTransactionCategoryID().getCategoryParentId() == 51) {

                        money51 += Math.abs(tran.getMoneyTradingWithSign());
                    }

                    if (tran.getTransactionCategoryID().getCategoryParentId() == 53) {

                        money53 += Math.abs(tran.getMoneyTradingWithSign());
                    }

                    if (tran.getTransactionCategoryID().getCategoryParentId() == 56) {

                        money56 += Math.abs(tran.getMoneyTradingWithSign());
                    }
                }

                listItemsIn.add(new Pair<>(money51, "Thưởng"));
                listItemsIn.add(new Pair<>(money53, "Lương"));
                listItemsIn.add(new Pair<>(money56, "Khoản thu khác"));

                for (int i = 0; i < listItemsIn.size(); i++) {

                    if (listItemsIn.get(i).first > 0) {

                        totalItemsIn.add(new Pair<>(listItemsIn.get(i).first, listItemsIn.get(i).second));
//                        mYData.add(listItemsIn.get(i).first);
//                        mXData.add(listItemsIn.get(i).second);
                    }
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
