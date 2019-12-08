package com.minhtzy.moneytracker.statistical.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.dataaccess.CategoriesDAOImpl;
import com.minhtzy.moneytracker.dataaccess.ICategoriesDAO;
import com.minhtzy.moneytracker.entity.CategoryEntity;
import com.minhtzy.moneytracker.utilities.ResourceUtils;
import com.minhtzy.moneytracker.view.CurrencyTextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class ChartOVTransactionAdapter extends RecyclerView.Adapter<ChartOVTransactionAdapter.ViewHolder> {

    private List<Pair<Float, String>> mItems = new ArrayList<>();

    private ICategoriesDAO mICategoriesDAO;

    public ChartOVTransactionAdapter(List<Pair<Float, String>> totalItems) {

        if (totalItems != null) {

            mItems.addAll(totalItems);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        mICategoriesDAO = new CategoriesDAOImpl(context);

        View view = LayoutInflater.from(context).inflate(R.layout.chart_ov_transaction_adpter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Pair<Float, String> item = mItems.get(position);
        holder.onBindView(item);

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImgItem;

        private TextView mLabelItem;

        private CurrencyTextView mTradingItem;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            mImgItem = itemView.findViewById(R.id.imgItem);
            mLabelItem = itemView.findViewById(R.id.txtLable);
            mTradingItem = itemView.findViewById(R.id.txtTranding);
        }

        void onBindView(Pair<Float, String> item) {

            mLabelItem.setText(item.second);
            mTradingItem.setText(String.valueOf(item.first));

            CategoryEntity category = mICategoriesDAO.getCategoryByName(item.second);

            mImgItem.setImageDrawable(ResourceUtils.getCategoryIcon(category.getCategoryIcon()));
        }
    }
}
