package com.example.t2m.moneytracker.budget;

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

import com.example.t2m.moneytracker.R;
import com.example.t2m.moneytracker.dataaccess.TransactionsDAOImpl;
import com.example.t2m.moneytracker.model.Budget;
import com.example.t2m.moneytracker.model.DateRange;
import com.example.t2m.moneytracker.model.MTDate;
import com.example.t2m.moneytracker.model.Transaction;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

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
    Budget mBudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_budget);

        Intent intent = getIntent();
        if (intent != null) {
            mBudget = (Budget) intent.getSerializableExtra(EXTRA_BUDGET);
        }

        addControls();
        addEvents();
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

            }
        });
    }

    private void updateUI() {
        if(mBudget == null) return;
        textGoal.setText(mBudget.getCategory().getCategory());
        // lấy ảnh từ asset
        String base_path = "category/";
        try {
            Drawable img = Drawable.createFromStream(getAssets().open(base_path + mBudget.getCategory().getIcon()), null);
            iconGoal.setImageDrawable(img);
        } catch (IOException e) {
            e.printStackTrace();
        }
        textBudgetAmount.setText(String.valueOf(mBudget.getAmount()));
        textNumSpent.setText(String.valueOf(mBudget.getSpent()));
        textNumRemain.setText(String.valueOf(mBudget.getAmount() - mBudget.getSpent()));

        int progress = (int) Math.ceil(mBudget.getSpent() / mBudget.getAmount() * 100);
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
        textDateInfo.setText(textLeft);
        iconWallet.setImageDrawable(getResources().getDrawable(R.drawable.ic_account_balance_wallet_black_24dp));
        textWalletName.setText(mBudget.getWallet().getWalletName());

        long total_days = (long)Math.ceil((mBudget.getTimeEnd().getMillis() - mBudget.getTimeStart().getMillis()) / 24 / 60 / 60/ 1000.0f) ;
        long spent_days = total_days;
        if(remainDays >0) {
            spent_days -= remainDays;
        }
        if(spent_days == 0) spent_days = total_days;
        float recommended= mBudget.getAmount() / total_days;
        float projected = mBudget.getSpent() * total_days / spent_days;
        float actual = mBudget.getSpent() / spent_days;
        textAmountRecomended.setText(String.valueOf(recommended));
        textAmountProjected.setText(String.valueOf(projected));
        textAmountActual.setText(String.valueOf(actual));

        //;

        loadChartTransactions();
    }


    private void loadChartTransactions() {

        TransactionsDAOImpl iTransactionsDAO = new TransactionsDAOImpl(this);
        List<Transaction> transactions = iTransactionsDAO.getStatisticalByCategoryInRange(
                mBudget.getWallet().getWalletId(),
                mBudget.getCategory().getId(),
                new DateRange(mBudget.getTimeStart(),mBudget.getTimeEnd()));
        List<Entry> entries = filterAmountByDates(transactions);

        chartTransactions.setDrawGridBackground(false);
        chartTransactions.getDescription().setEnabled(false);

        LineDataSet dataSet = new LineDataSet(entries,"đ");
        LineData lineData = new LineData(dataSet);
        chartTransactions.setData(lineData);
        chartTransactions.invalidate();


    }

    private List<Entry>  filterAmountByDates(List<Transaction> transactions) {
        long start = mBudget.getTimeStart().getMillis();
        long end = mBudget.getTimeEnd().getMillis();

        int total_day = (int) Math.ceil((end - start) / 24 / 60/60/1000.0f);
        List<Entry> entries = new ArrayList<>();
        for(int i = 0; i <= total_day; ++i) {
            entries.add(new Entry(i+1,0.0f));
        }

        for(Transaction t : transactions) {
            long current = t.getTransactionDate().getTime();
            int index = (int) Math.ceil((current - start) / 24 / 60/60/1000.0f);
            entries.get(index).setY(entries.get(index).getY() + t.getMoneyTrading());

        }

        float total = 0.0f;
        for (int i = 0; i < entries.size(); ++i) {
            total += entries.get(i).getY();
            entries.get(i).setY(total);
        }

        return entries;
    }

}
