package com.minhtzy.moneytracker.view.budget;

import android.view.View;

public interface OnBudgetItemClickListener {

    void onItemClick(View view, int position);
    void onItemLongClick(View view,int position);

}