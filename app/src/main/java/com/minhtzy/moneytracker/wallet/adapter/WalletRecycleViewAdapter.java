package com.minhtzy.moneytracker.wallet.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.entity.WalletEntity;
import com.minhtzy.moneytracker.utilities.ResourceUtils;
import com.minhtzy.moneytracker.utilities.SharedPrefs;
import com.minhtzy.moneytracker.view.CurrencyTextView;
import com.minhtzy.moneytracker.wallet.OnWalletItemInteractionListener;

import java.util.List;

public class WalletRecycleViewAdapter extends RecyclerView.Adapter<WalletRecycleViewAdapter.ViewHolder> {

    private final List<WalletEntity> mValues;
    private final OnWalletItemInteractionListener mListener;

    String mCurrentWalletId;

    public WalletRecycleViewAdapter(List<WalletEntity> mValues, OnWalletItemInteractionListener mListener) {
        this.mValues = mValues;
        this.mListener = mListener;
        mCurrentWalletId = SharedPrefs.getInstance().get(SharedPrefs.KEY_CURRENT_WALLET,new String());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.wallet_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTextName.setText(mValues.get(position).getName());
        holder.mTextAmount.setCurrrencyCode(mValues.get(position).getCurrencyCode());
        holder.mTextAmount.setText(String.valueOf(mValues.get(position).getCurrentBalance()));
        holder.mIcon.setImageDrawable(ResourceUtils.getWalletIcon((mValues.get(position).getIcon())));
        holder.mChecked.setVisibility(holder.mItem.getWalletId().equals(mCurrentWalletId) ? ImageView.VISIBLE  : ImageView.INVISIBLE);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onWalletItemClicked(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        public final TextView mTextName;
        public final CurrencyTextView mTextAmount;
        public final ImageView mIcon;
        public final ImageView mChecked;
        public WalletEntity mItem;

        public ViewHolder(View view) {
            super(view);
            this.mView = view;
            mTextName = view.findViewById(R.id.tvName);
            mTextAmount =view.findViewById(R.id.tvAmount);
            mIcon = view.findViewById(R.id.ivIcon);
            mChecked = view.findViewById(R.id.ivCheck);
        }
    }
}
