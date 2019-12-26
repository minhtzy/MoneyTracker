package com.minhtzy.moneytracker.view.statistical;

import android.os.Bundle;


import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.entity.TransactionEntity;
import com.minhtzy.moneytracker.model.DateRange;
import com.minhtzy.moneytracker.model.MTDate;
import com.minhtzy.moneytracker.utilities.DateUtils;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentBarChartOv extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ITEMS = "items";

    private static final String ARG_POSITION = "position";

    // TODO: Rename and change types of parameters

    private List<TransactionEntity> mItems = new ArrayList<>();

    private BarChart mBarChart;

    private TextView mDate;

    private DateRange mDateRange;

    private DateUtils mDateUtils = new DateUtils();

    private List<Float> mTranding = new ArrayList<>();

    private List<String> mTitleMonth = new ArrayList<>();

    private Calendar mCalendar = Calendar.getInstance();

    public FragmentBarChartOv() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    static FragmentBarChartOv newInstance(List<TransactionEntity> listTransaction, int position) {
        FragmentBarChartOv fragment = new FragmentBarChartOv();
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

            mItems = Parcels.unwrap(getArguments().getParcelable(ARG_ITEMS));

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bar_chart_ov, container, false);
        mBarChart = view.findViewById(R.id.barchartItems);
        mDate = view.findViewById(R.id.txtDate);
//        RecyclerView mRecycleViewOV = view.findViewById(R.id.recycleViewOV);
        mDateRange = new DateRange(new MTDate(mCalendar.get(Calendar.YEAR), 0, 1).firstDayOfMonth().setTimeToBeginningOfDay(), new MTDate());

        onBindView();
        initEvents();

        return view;
    }

    private void initEvents() {

    }

    private void onBindView() {

        mDate.setText(String.valueOf(mCalendar.get(Calendar.YEAR)));

        if (mItems != null) {

            int year, month;
            int from_month = mDateRange.getDateFrom().getMonth();
            int from_year = mDateRange.getDateFrom().getYear();
            int to_month = mDateRange.getDateTo().getMonth();
            int to_year = mDateRange.getDateTo().getYear();

            for (year = from_year; year <= to_year; ++year) {
                for (month = 0; month < 12; ++month) {
                    if (year == from_year && month < from_month) continue;
                    if (year == to_year && month > to_month) break;
                    MTDate firstDay = new MTDate(year, month, 1).firstDayOfMonth().setTimeToBeginningOfDay();
                    MTDate lastDay = new MTDate(year, month, 1).lastDayOfMonth().setTimeToEndOfDay();
                    DateRange period = new DateRange(firstDay, lastDay);

                    getTransactionOfMonth(period);
                }
            }

            addBarEntries();
        }


    }

    private void addBarEntries() {
        BarData data = new BarData(getDataSet());

        mTitleMonth.add("thg1");
        mTitleMonth.add("thg2");
        mTitleMonth.add("thg3");
        mTitleMonth.add("thg4");
        mTitleMonth.add("thg5");
        mTitleMonth.add("thg6");
        mTitleMonth.add("thg7");
        mTitleMonth.add("thg8");
        mTitleMonth.add("thg9");
        mTitleMonth.add("thg10");
        mTitleMonth.add("thg11");
        mTitleMonth.add("thg12");

        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(mTitleMonth));
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//
        mBarChart.setData(data);
        mBarChart.animateY(4000);
        mBarChart.setFitBars(true); // make the x-axis fit exactly all bars
        mBarChart.invalidate(); // refresh
    }

    private IBarDataSet getDataSet() {

        List<BarEntry> NoOfEmpTrading = new ArrayList<>();

        for (int i = 0; i < mTranding.size(); i++) {

            NoOfEmpTrading.add(new BarEntry(i, mTranding.get(i)));
        }

        BarDataSet bardataset = new BarDataSet(NoOfEmpTrading, "Biểu đồ giao dịch");
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);


        return bardataset;
    }


    private void getTransactionOfMonth(DateRange period) {

        List<TransactionEntity> transactions = filterTransactions(period, mItems);
        float trading = 0;
        if (transactions != null) {

            for (TransactionEntity tran : transactions) {

                trading += Math.abs(tran.getTransactionAmount());
            }

        }
        mTranding.add(trading);

    }

    private List<TransactionEntity> filterTransactions(DateRange dateRange, List<TransactionEntity> transactions) {
        List<TransactionEntity> filter = new ArrayList<>();
        for (TransactionEntity t : transactions) {
            if (mDateUtils.isDateRangeContainDate(dateRange, t.getTransactionTime())) {
                filter.add(t);
            }
        }
        return filter;
    }

}
