package com.minhtzy.moneytracker.budget;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.dataaccess.TransactionsDAOImpl;
import com.minhtzy.moneytracker.entity.BudgetEntity;
import com.minhtzy.moneytracker.entity.CategoryEntity;
import com.minhtzy.moneytracker.entity.TransactionEntity;
import com.minhtzy.moneytracker.entity.WalletEntity;
import com.minhtzy.moneytracker.model.DateRange;
import com.minhtzy.moneytracker.model.MTDate;
import com.minhtzy.moneytracker.transaction.ViewTransactionListActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.minhtzy.moneytracker.utilities.CategoryManager;
import com.minhtzy.moneytracker.utilities.WalletsManager;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetailBudgetActivity extends AppCompatActivity {

    public static String EXTRA_BUDGET = "Extra_DetailBudgetActivity_BUDGET";

    TextView textGoal;
    ImageView iconGoal;
    TextView textBudgetAmount;
    TextView textNumSpent;
    TextView textNumRemain;
    ProgressBar budgetProgressBar;
    TextView textDate;
    TextView textDateInfo;
    ImageView iconWallet;
    TextView textWalletName;
    LineChart chartTransactions;
    TextView textAmountRecomended;
    TextView textAmountProjected;
    TextView textAmountActual;
    TextView textListTransaction;
    BudgetEntity mBudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_budget);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if (intent != null) {
            mBudget = (BudgetEntity) Parcels.unwrap(intent.getParcelableExtra(EXTRA_BUDGET));
        }

        addControls();
        addEvents();
    }

    public boolean onSupportNavigateUp() {
        onClickedCancle();
        return true;
    }

    private void onClickedCancle() {
        setResult(RESULT_CANCELED);
        finish();
    }
    private void addControls() {

        textGoal = findViewById(R.id.title);
        iconGoal = findViewById(R.id.icon);
        textBudgetAmount = findViewById(R.id.txt_budget_category_detail_num_budget);
        textNumSpent = findViewById(R.id.txt_budget_category_detail_num_spent);
        textNumRemain = findViewById(R.id.txt_budget_category_detail_num_left);
        budgetProgressBar = findViewById(R.id.progress_budget_category_detail);
        textDate = findViewById(R.id.date);
        textDateInfo = findViewById(R.id.date_info);
        iconWallet = findViewById(R.id.wallet_icon);
        textWalletName = findViewById(R.id.wallet_name);
        chartTransactions = findViewById(R.id.group_item_budget_category_detail);
        textAmountRecomended = findViewById(R.id.txvAmountRecommended);
        textAmountProjected = findViewById(R.id.txvAmountProjected);
        textAmountActual = findViewById(R.id.txvAmountActual);
        textListTransaction = findViewById(R.id.btnViewTransaction);

        updateUI();
    }

    private void addEvents() {
        textListTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransactionsDAOImpl iTransactionsDAO = new TransactionsDAOImpl(DetailBudgetActivity.this);
                List<TransactionEntity> transactions = iTransactionsDAO.getAllTransactionByCategoryInRange(
                        mBudget.getWalletId(),
                        mBudget.getCategoryId(),
                        mBudget.getPeriod());
                Intent intent = new Intent(DetailBudgetActivity.this,ViewTransactionListActivity.class);
                intent.putExtra(ViewTransactionListActivity.BUNDLE_LIST_ITEM, (ArrayList<TransactionEntity>) transactions);
                startActivity(intent);

            }
        });
    }

    private void updateUI() {
        if(mBudget == null) return;
        CategoryEntity category = CategoryManager.getInstance().getCategoryById(mBudget.getCategoryId());
        textGoal.setText(category.getCategoryName());
        // lấy ảnh từ asset
        String base_path = "category/";
        try {
            Drawable img = Drawable.createFromStream(getAssets().open(base_path + category.getCategoryIcon()), null);
            iconGoal.setImageDrawable(img);
        } catch (IOException e) {
            e.printStackTrace();
        }
        textBudgetAmount.setText(String.valueOf(mBudget.getBudgetAmount()));
        textNumSpent.setText(String.valueOf(mBudget.getSpent()));
        textNumRemain.setText(String.valueOf(mBudget.getBudgetAmount() - mBudget.getSpent()));

        int progress = (int) Math.ceil(mBudget.getSpent() / mBudget.getBudgetAmount() * 100);
        budgetProgressBar.setMax(100);
        budgetProgressBar.setMax(100);
        budgetProgressBar.setProgress(progress);

        TextView txtRemain = findViewById(R.id.txt_budget_category_detail_left);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (progress <= 100) {
                txtRemain.setText(getResources().getString(R.string.transaction_detail_cashback_left));
                budgetProgressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorMoneyTradingPositive)));
            } else {
                txtRemain.setText(getResources().getString(R.string.transaction_detail_cashback_over));
                budgetProgressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorMoneyTradingNegative)));
            }
        }
        textDate.setText("");
        String textLeft = getString(R.string.remain_prefix);
        MTDate now = new MTDate().today();
        long remainDays = (long) Math.ceil((mBudget.getTimeEnd().getMillis() - now.getMillis()) / 24 / 60 / 60 / 1000.0f) ;
        if (remainDays > 0) {
            textLeft += " " + remainDays + " " + getString(R.string.day);
        }
        else {
            textLeft = getString(R.string.finnished);
        }

        WalletEntity wallet = WalletsManager.getInstance(this).getWalletById(mBudget.getWalletId());
        textDateInfo.setText(textLeft);
        iconWallet.setImageDrawable(getResources().getDrawable(R.drawable.ic_account_balance_wallet_black_24dp));
        textWalletName.setText(wallet.getName());

        long total_days = (long)Math.ceil((mBudget.getTimeEnd().getMillis() - mBudget.getTimeStart().getMillis()) / 24 / 60 / 60/ 1000.0f) ;
        long spent_days = total_days;
        if(remainDays >0) {
            spent_days -= remainDays;
        }
        if(spent_days == 0) spent_days = total_days;
        double recommended= mBudget.getBudgetAmount() / total_days;
        double projected = mBudget.getSpent() * total_days / spent_days;
        double actual = mBudget.getSpent() / spent_days;
        textAmountRecomended.setText(String.valueOf(recommended));
        textAmountProjected.setText(String.valueOf(projected));
        textAmountActual.setText(String.valueOf(actual));

        //;

        loadChartTransactions();
    }


    private void loadChartTransactions() {

        TransactionsDAOImpl iTransactionsDAO = new TransactionsDAOImpl(this);
        List<TransactionEntity> transactions = iTransactionsDAO.getStatisticalByCategoryInRange(
                mBudget.getWalletId(),
                mBudget.getCategoryId(),
                mBudget.getPeriod());
        List<Entry> entries = filterAmountByDates(transactions);

        chartTransactions.setDrawGridBackground(true);
        chartTransactions.getDescription().setEnabled(false);

        LineDataSet dataSet = new LineDataSet(entries,"Chi tiêu");


        ArrayList<ILineDataSet> datasets = new ArrayList<ILineDataSet>();
        datasets.add(dataSet);

        LineData lineData = new LineData(dataSet);
        chartTransactions.setData(lineData);
        chartTransactions.setMinimumHeight(500);
        chartTransactions.invalidate();

        String values[] = new String[entries.size()];
        for(int i = 0 ; i < values.length; ++i) {
            values[i] = " ";
        }

        values[0] = mBudget.getTimeStart().toIsoDateString();
        values[values.length - 1] = mBudget.getTimeEnd().toIsoDateString();
        XAxis xAxis = chartTransactions.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(values));

        chartTransactions.setMaxHighlightDistance((float)mBudget.getBudgetAmount());

        chartTransactions.getAxisLeft().setAxisMinimum(0.0f);
        chartTransactions.getAxisLeft().setAxisMaximum(entries.get(entries.size() - 1).getY() + 200);

        chartTransactions.getAxisRight().setEnabled(false);

        LimitLine limitLine = new LimitLine((float)mBudget.getBudgetAmount(),"Ngân sách");
        limitLine.setLineWidth(4f);
        limitLine.enableDashedLine(10f, 10f, 0f);
        limitLine.setTextSize(10f);
        chartTransactions.getAxisLeft().addLimitLine(limitLine);


    }

    private List<Entry>  filterAmountByDates(List<TransactionEntity> transactions) {
        long start = mBudget.getTimeStart().getMillis();
        long end = mBudget.getTimeEnd().getMillis();

        int total_day = (int) Math.ceil((end - start) / 24 / 60/60/1000.0f);
        List<Entry> entries = new ArrayList<>();
        for(int i = 0; i <= total_day; ++i) {
            entries.add(new Entry(i,0.0f));
        }

        for(TransactionEntity t : transactions) {
            long current = t.getTransactionTime().getTime();
            int index = (int) Math.ceil((current - start) / 24 / 60/60/1000.0f);
            entries.get(index).setY(entries.get(index).getY() + (float)t.getTransactionAmount());

        }

        float total = 0.0f;
        for (int i = 0; i < entries.size(); ++i) {
            total += entries.get(i).getY();
            entries.get(i).setY(total);
        }

        return entries;
    }

}
