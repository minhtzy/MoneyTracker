package com.minhtzy.moneytracker.view.statistical;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Pair;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


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
import com.minhtzy.moneytracker.model.TransactionTypes;
import com.minhtzy.moneytracker.utilities.CategoryManager;
import com.minhtzy.moneytracker.utilities.ResourceUtils;
import com.minhtzy.moneytracker.view.CurrencyTextView;
import com.minhtzy.moneytracker.entity.TransactionEntity;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OVTransactionMonth extends AppCompatActivity {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "OVTransactionMonth";

    public static final String ARG_ITEMS = "items";
    public static final String TITLE = "title";

    // TODO: Rename and change types of parameters

    private CurrencyTextView mMoneyIncome;

    private CurrencyTextView mMoneyExpenses;

    private CurrencyTextView mMoneyIncomeNet;

    private ImageView mImageExMax;

    private TextView mLabelExMax;

    private TextView mNoteExMax;

    private TextView mTradingExMax;

    private ImageView mImageExMin;

    private TextView mLabelExMin;

    private TextView mNoteExMin;

    private TextView mTradingExMin;

    private ImageView mImageInMax;

    private TextView mLabelInMax;

    private TextView mNoteInMax;

    private TextView mTradingInMax;

    private ImageView mImageInMin;

    private TextView mLabelInMin;

    private TextView mNoteInMin;

    private TextView mTradingInMin;

    private PieChart mPiechartEx;

    private PieChart mPiechartIn;

    private RelativeLayout mLayout1;

    private RelativeLayout mLayout2;

    private RelativeLayout mLayout3;

    private RelativeLayout mLayout4;

    private List<TransactionEntity> mItems = new ArrayList<>();

    private final OnChartValueSelectedListener mPiechartExListener = new OnChartValueSelectedListener() {
        @Override
        public void onValueSelected(Entry e, Highlight h) {

//            showItemSelect(e, h, mYDataEx, mXDataEx);


        }

        @Override
        public void onNothingSelected() {

        }
    };

    private final OnChartValueSelectedListener mPiechartInListener = new OnChartValueSelectedListener() {
        @Override
        public void onValueSelected(Entry e, Highlight h) {

//            showItemSelect(e, h, mYDataIn, mXDataIn);
        }

        @Override
        public void onNothingSelected() {

        }
    };

    public OVTransactionMonth() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ov_transaction_month);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent().hasExtra(ARG_ITEMS)) {

            mItems = (List<TransactionEntity>) Parcels.unwrap(getIntent().getParcelableExtra(ARG_ITEMS));
        }

        if(getIntent().hasExtra(TITLE))
        {
            setTitle(getIntent().getStringExtra(TITLE));
        }

        addControls();

        onBindView();

        initEvents();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void addControls() {
        mMoneyIncome = findViewById(R.id.txtIncome);
        mMoneyExpenses = findViewById(R.id.txtExpenses);
        mMoneyIncomeNet = findViewById(R.id.txtIncomeNet);

        mImageExMax = findViewById(R.id.imgItemExMax);
        mLabelExMax = findViewById(R.id.txtLableExMax);
        mTradingExMax = findViewById(R.id.txtTrandingExMax);

        mImageExMin = findViewById(R.id.imgItemExMin);
        mLabelExMin = findViewById(R.id.txtLableExMin);
        mTradingExMin = findViewById(R.id.txtTrandingExMin);

        mImageInMax = findViewById(R.id.imgItemInMax);
        mLabelInMax = findViewById(R.id.txtLableInMax);
        mTradingInMax = findViewById(R.id.txtTrandingInMax);

        mImageInMin = findViewById(R.id.imgItemInMin);
        mLabelInMin = findViewById(R.id.txtLableInMin);
        mTradingInMin = findViewById(R.id.txtTrandingInMin);

        mPiechartEx = findViewById(R.id.piechartExpenses);

        mPiechartEx.setRotationEnabled(true);
        mPiechartEx.getDescription().setText("Biểu đồ thống kê giao dịch");
        mPiechartEx.setHoleRadius(35f);
        mPiechartEx.setTransparentCircleAlpha(0);
        mPiechartEx.setCenterText("Khoản chi");
        mPiechartEx.setCenterTextSize(10);
        mPiechartEx.setDrawEntryLabels(true);

        mPiechartEx.setDrawSliceText(false); // To remove slice text
        mPiechartEx.setDrawMarkers(false); // To remove markers when click
        mPiechartEx.setDrawEntryLabels(false); // To remove labels from piece of pie
        mPiechartEx.getDescription().setEnabled(false);

        mPiechartIn = findViewById(R.id.piechartIncome);
        mPiechartEx.getDescription().setText("Biểu đồ thống kê giao dịch");
        ;
        mPiechartIn.setHoleRadius(35f);
        mPiechartIn.setTransparentCircleAlpha(0);
        mPiechartIn.setCenterText("Khoản thu");
        mPiechartIn.setCenterTextSize(10);
        mPiechartIn.setDrawEntryLabels(true);

        mPiechartIn.setDrawSliceText(false); // To remove slice text
        mPiechartIn.setDrawMarkers(false); // To remove markers when click
        mPiechartIn.setDrawEntryLabels(false); // To remove labels from piece of pie
        mPiechartIn.getDescription().setEnabled(false);


        mLayout1 = findViewById(R.id.layout_ov1);
        mLayout2 = findViewById(R.id.layout_ov2);
        mLayout3 = findViewById(R.id.layout_ov3);
        mLayout4 = findViewById(R.id.layout_ov4);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initEvents() {

        mPiechartEx.setOnChartValueSelectedListener(mPiechartExListener);
        mPiechartIn.setOnChartValueSelectedListener(mPiechartInListener);
    }

    private void onBindView() {

        float moneyEx = 0;
        float moneyIn = 0;

        List<TransactionEntity> itemsEx = new ArrayList<>();
        List<TransactionEntity> itemsIn = new ArrayList<>();


        List<Pair<Float, String>> totalDataEx = new ArrayList<>();
        List<Pair<Float, String>> totalDataIn = new ArrayList<>();

        SparseArray<Float> listSumAmount = new SparseArray<>();
        for (TransactionEntity tran : mItems) {
            int parentID = tran.getCategoryId();
            CategoryEntity categoryEntity = CategoryManager.getInstance().getCategoryById(parentID);
            try {
                parentID = categoryEntity.getParentId();
            } catch (NullPointerException ex) {

            }
            float value = listSumAmount.get(parentID) != null ? listSumAmount.get(parentID) : 0;
            float new_value = value + (float) Math.abs(tran.getTransactionAmount());

            listSumAmount.put(parentID, new_value);

            if (tran.getTransactionAmount() < 0) {

                moneyEx += Math.abs(tran.getTransactionAmount());
                itemsEx.add(tran);
            } else {

                moneyIn += Math.abs(tran.getTransactionAmount());
                itemsIn.add(tran);
            }
        }

        ICategoriesDAO categoriesDAO = new CategoriesDAOImpl(this);


        List<CategoryEntity> listCateExpress = categoriesDAO.getCategoriesByType(TransactionTypes.EXPENSE.getValue());
        listCateExpress.addAll(categoriesDAO.getCategoriesByType(TransactionTypes.LOAN.getValue()));

        for (CategoryEntity categoryEntity : listCateExpress) {

            if (listSumAmount.indexOfKey(categoryEntity.getCategoryId()) >= 0 && listSumAmount.get(categoryEntity.getCategoryId()) > 0) {

                totalDataEx.add(new Pair<>(listSumAmount.get(categoryEntity.getCategoryId()), categoryEntity.getCategoryName()));
            }
        }

        List<CategoryEntity> listCateIncome = categoriesDAO.getCategoriesByType(TransactionTypes.INCOME.getValue());
        listCateIncome.addAll(categoriesDAO.getCategoriesByType(TransactionTypes.DEBIT.getValue()));

        for (CategoryEntity categoryEntity : listCateIncome) {

            if (listSumAmount.indexOfKey(categoryEntity.getCategoryId()) >= 0 && listSumAmount.get(categoryEntity.getCategoryId()) > 0)
                totalDataIn.add(new Pair<>(listSumAmount.get(categoryEntity.getCategoryId()), categoryEntity.getCategoryName()));
        }


        float moneyIncomeNet = moneyIn - moneyEx;


        mMoneyIncome.setText(String.valueOf(moneyIn));
        mMoneyIncome.setTextColor(getResources().getColor(R.color.colorMoneyTradingPositive));

        mMoneyExpenses.setText(String.valueOf(moneyEx));
        mMoneyExpenses.setTextColor(getResources().getColor(R.color.colorMoneyTradingNegative));

        mMoneyIncomeNet.setText(String.valueOf(moneyIncomeNet));
        if (moneyIncomeNet >= 0) {

            mMoneyIncomeNet.setTextColor(getResources().getColor(R.color.colorMoneyTradingPositive));
        } else {

            mMoneyIncomeNet.setTextColor(getResources().getColor(R.color.colorMoneyTradingNegative));
        }

        if (!itemsEx.isEmpty()) {

            onBindViewExMax(itemsEx);
            onBindViewExMin(itemsEx);
        } else {

            mLayout1.setVisibility(View.GONE);
            mLayout3.setVisibility(View.GONE);
        }

        if (!itemsIn.isEmpty()) {

            onBindViewInMax(itemsIn);
            onBindViewInMin(itemsIn);
        } else {

            mLayout2.setVisibility(View.GONE);
            mLayout4.setVisibility(View.GONE);
        }

        addDataSet(mPiechartEx, totalDataEx);
        addDataSet(mPiechartIn, totalDataIn);
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

    private void onBindViewExMax(final List<TransactionEntity> itemsEx) {

        // lấy item có properties trading lớn nhất
        TransactionEntity transaction = Collections.max(itemsEx, new Comparator<TransactionEntity>() {
            @Override
            public int compare(TransactionEntity o1, TransactionEntity o2) {
                if (o1.getTransactionAmount() == o2.getTransactionAmount()) return 0;
                return (Math.abs(o1.getTransactionAmount()) > Math.abs(o2.getTransactionAmount())) ? 1 : -1;
            }
        });
        CategoryEntity category = CategoryManager.getInstance().getCategoryById(transaction.getCategoryId());
        mLabelExMax.setText(category.getCategoryName());
        mTradingExMax.setText(String.valueOf(transaction.getTransactionAmount()));
        mTradingExMax.setTextColor(getResources().getColor(R.color.colorMoneyTradingNegative));

        mImageExMax.setImageDrawable(ResourceUtils.getCategoryIcon(category.getCategoryIcon()));
    }

    private void onBindViewExMin(List<TransactionEntity> itemsEx) {

        TransactionEntity transaction = Collections.min(itemsEx, new Comparator<TransactionEntity>() {
            @Override
            public int compare(TransactionEntity o1, TransactionEntity o2) {
                if (o1.getTransactionAmount() == o2.getTransactionAmount()) return 0;
                return (Math.abs(o1.getTransactionAmount()) > Math.abs(o2.getTransactionAmount())) ? 1 : -1;
            }
        });
        CategoryEntity category = CategoryManager.getInstance().getCategoryById(transaction.getCategoryId());
        mLabelExMin.setText(category.getCategoryName());
        mTradingExMin.setText(String.valueOf(transaction.getTransactionAmount()));
        mTradingExMin.setTextColor(getResources().getColor(R.color.colorMoneyTradingNegative));
        mImageExMin.setImageDrawable(ResourceUtils.getCategoryIcon(category.getCategoryIcon()));
    }

    private void onBindViewInMax(List<TransactionEntity> itemsIn) {

        TransactionEntity transaction = Collections.max(itemsIn, new Comparator<TransactionEntity>() {
            @Override
            public int compare(TransactionEntity o1, TransactionEntity o2) {
                if (o1.getTransactionAmount() == o2.getTransactionAmount()) return 0;
                return (o1.getTransactionAmount() > o2.getTransactionAmount()) ? 1 : -1;
            }
        });
        CategoryEntity category = CategoryManager.getInstance().getCategoryById(transaction.getCategoryId());
        mLabelInMax.setText(category.getCategoryName());
        mTradingInMax.setText(String.valueOf(transaction.getTransactionAmount()));
        mTradingInMax.setTextColor(getResources().getColor(R.color.colorMoneyTradingPositive));

        mImageInMax.setImageDrawable(ResourceUtils.getCategoryIcon(category.getCategoryIcon()));
    }

    private void onBindViewInMin(List<TransactionEntity> itemsIn) {

        TransactionEntity transaction = Collections.min(itemsIn, new Comparator<TransactionEntity>() {
            @Override
            public int compare(TransactionEntity o1, TransactionEntity o2) {
                if (o1.getTransactionAmount() == o2.getTransactionAmount()) return 0;
                return (o1.getTransactionAmount() > o2.getTransactionAmount()) ? 1 : -1;
            }
        });
        CategoryEntity category = CategoryManager.getInstance().getCategoryById(transaction.getCategoryId());
        mLabelInMin.setText(category.getCategoryName());
        mTradingInMin.setText(String.valueOf(transaction.getTransactionAmount()));
        mTradingInMin.setTextColor(getResources().getColor(R.color.colorMoneyTradingPositive));

        mImageInMin.setImageDrawable(ResourceUtils.getCategoryIcon(category.getCategoryIcon()));
    }

}
