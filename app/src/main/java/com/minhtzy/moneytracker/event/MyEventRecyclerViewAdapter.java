package com.minhtzy.moneytracker.event;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.entity.EventEntity;
import com.minhtzy.moneytracker.model.MTDate;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link EventEntity} and makes a call to the
 * specified {@link OnEventItemInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyEventRecyclerViewAdapter extends RecyclerView.Adapter<MyEventRecyclerViewAdapter.ViewHolder> {

    private final List<EventEntity> mValues;
    private final OnEventItemInteractionListener mListener;

    public MyEventRecyclerViewAdapter(List<EventEntity> items, OnEventItemInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(mValues.get(position).getEventName());
        int remainDate = getRemainDate(mValues.get(position).getTimeExpire());
        holder.mRemainDate.setText(remainDate);
        holder.mSpentAmount.setText(String.valueOf(mValues.get(position).getSpentAmount()));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onEventItemClicked(holder.mItem);
                }
            }
        });
    }

    private int getRemainDate(MTDate timeExpire) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mRemainDate;
        public final TextView mSpentAmount;
        public final ImageView mIcon;
        public EventEntity mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.event_name);
            mRemainDate = (TextView) view.findViewById(R.id.remain_date);
            mSpentAmount = view.findViewById(R.id.spent_amount);
            mIcon = view.findViewById(R.id.icon_event);
        }
    }
}
