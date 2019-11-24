package com.minhtzy.moneytracker.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.budget.OnBudgetItemClickListener;
import com.minhtzy.moneytracker.entity.BudgetEntity;
import com.minhtzy.moneytracker.entity.CategoryEntity;
import com.minhtzy.moneytracker.model.DateRange;
import com.minhtzy.moneytracker.model.MTDate;
import com.minhtzy.moneytracker.utilities.ResourceUtils;
import com.minhtzy.moneytracker.utilities.CategoryManager;

import java.util.List;

public class BudgetsAdapter extends RecyclerView.Adapter<BudgetsAdapter.ViewHolder> {


    List<BudgetEntity> mBudgets;

    public void setListener(OnBudgetItemClickListener listener) {
        this.listener = listener;
    }

    OnBudgetItemClickListener listener;

    public BudgetsAdapter(List<BudgetEntity> budgets) {
        mBudgets = budgets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_budget_overview, viewGroup, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        BudgetEntity budget = mBudgets.get(i);
        Context context = viewHolder.itemView.getContext();
        // lấy ảnh từ asset
        viewHolder.iconGoal.setImageDrawable(ResourceUtils.getCategoryIcon(budget.getBudgetIcon()));

        CategoryEntity category = CategoryManager.getInstance().getCategoryById(budget.getCategoryId());
        viewHolder.textBudgetTitle.setText(category.getCategoryName());
        viewHolder.textTimeRange.setText(new DateRange(budget.getPeriod().getDateFrom(), budget.getPeriod().getDateTo()).toString());

        String textLeft = context.getString(R.string.remain_prefix);
        MTDate now = new MTDate();

        long remainDays =(long) Math.ceil( (budget.getPeriod().getDateTo().getMillis() - now.getMillis()) / 24 / 60 / 60 / 1000.0f);
        if (remainDays > 0) {
            textLeft += " " + remainDays + " " + context.getString(R.string.day);
        }
        viewHolder.textTimeLeft.setText(textLeft);

        float remain = (float) (budget.getBudgetAmount() - budget.getSpent());
        if(remain < 0) {
            remain = Math.abs(remain);
            viewHolder.textCurrent.setText(context.getString(R.string.transaction_detail_cashback_over));
        }
        else {
            viewHolder.textCurrent.setText(context.getString(R.string.transaction_detail_cashback_left));
        }
        viewHolder.textAmountLeft.setText(String.valueOf(remain));
        int progress = (int) (budget.getSpent() / budget.getBudgetAmount() * 100);
        viewHolder.progressBudget.setMax(100);
        viewHolder.progressBudget.setProgress(progress);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (progress <= 100) {
                viewHolder.progressBudget.setProgressTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorMoneyTradingPositive)));
            } else {
                viewHolder.progressBudget.setProgressTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorMoneyTradingNegative)));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mBudgets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener , View.OnLongClickListener{
        ImageView iconGoal;
        ImageView iconWallet;
        TextView textBudgetTitle;
        TextView textTimeRange;
        TextView textAmountLeft;
        TextView textTimeLeft;
        TextView textCurrent;
        ProgressBar progressBudget;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iconGoal = itemView.findViewById(R.id.icon_goal);
            iconWallet = itemView.findViewById(R.id.iconWallet);
            textBudgetTitle = itemView.findViewById(R.id.txtBudgetTitle);
            textTimeRange = itemView.findViewById(R.id.txtTimeRanger);
            textTimeLeft = itemView.findViewById(R.id.txtTimeLeft);
            textCurrent = itemView.findViewById(R.id.current);
            textAmountLeft = itemView.findViewById(R.id.txtAmountLeft);
            progressBudget = itemView.findViewById(R.id.prgBudget);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(v,getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            listener.onItemLongClick(v,getAdapterPosition());
            return false;
        }


    }

}
