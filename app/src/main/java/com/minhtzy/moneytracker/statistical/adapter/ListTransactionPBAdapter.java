package com.minhtzy.moneytracker.statistical.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.entity.CategoryEntity;
import com.minhtzy.moneytracker.entity.TransactionEntity;
import com.minhtzy.moneytracker.model.MTDate;
import com.minhtzy.moneytracker.utilities.CategoryManager;
import com.minhtzy.moneytracker.utilities.ResourceUtils;
import com.minhtzy.moneytracker.view.CurrencyTextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class ListTransactionPBAdapter extends RecyclerView.Adapter<ListTransactionPBAdapter.ViewHoder> {

    private List<TransactionEntity> mItems = new ArrayList<>();

    public ListTransactionPBAdapter(List<TransactionEntity> items) {

        if (items != null) {

            mItems.addAll(items);
        }
    }

    @SuppressLint("SimpleDateFormat")
    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.snippet_transaction_item6, parent, false);

        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {

        TransactionEntity transaction = mItems.get(position);
        holder.onBindView(transaction);

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHoder extends RecyclerView.ViewHolder {

        private ImageView mImgCategory;

        private TextView mLable;

        private TextView mDate;

        private CurrencyTextView mTrading;

        ViewHoder(@NonNull View itemView) {
            super(itemView);

            mImgCategory = itemView.findViewById(R.id.imgTransactionItem);
            mLable = itemView.findViewById(R.id.txtTransactionLable);
            mDate = itemView.findViewById(R.id.txtTransactionDate);
            mTrading = itemView.findViewById(R.id.txtTrandingItem);
        }

        public void onBindView(TransactionEntity transaction) {

            CategoryEntity category = CategoryManager.getInstance().getCategoryById(transaction.getCategoryId());
            mLable.setText(category.getCategoryName());
            mTrading.setText(String.valueOf(transaction.getTransactionAmount()));

            mImgCategory.setImageDrawable(ResourceUtils.getCategoryIcon(category.getCategoryIcon()));
            mDate.setText(new MTDate(transaction.getTransactionTime()).toIsoDateString());

        }
    }
}
