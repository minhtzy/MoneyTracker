package com.example.t2m.moneytracker.transaction;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.t2m.moneytracker.R;
import com.example.t2m.moneytracker.pinnedlistview.SectionedBaseAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TransactionAdapter extends SectionedBaseAdapter {

    private Context mContext;
    private List<Pair<Date,List<Transaction>>> mItems;

    public void add(Date date,List<Transaction> list_item) {
        mItems.add(new Pair<Date, List<Transaction>>(date,list_item));
        super.notifyDataSetChanged();
    }

    public TransactionAdapter() {
        mItems = new ArrayList<>();
    }
    public TransactionAdapter(Context context) {
        mContext = context;
        mItems = new ArrayList<>();
    }
    @Override
    public Object getItem(int section, int position) {
        return mItems.get(section).second.get(position);
    }

    @Override
    public long getItemId(int section, int position) {

        int res = 0;
        for (int i = 0; i < section - 1; i++) {
            res += mItems.get(i).second.size();
            res += 1;
        }
        return res + position;
    }

    @Override
    public int getSectionCount() {
        return mItems.size();
    }

    @Override
    public int getCountForSection(int section) {
        return mItems.get(section).second.size();
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
        RelativeLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (RelativeLayout) inflator.inflate(R.layout.snippet_transaction_item, null);

            ItemViewHolder holder = new ItemViewHolder();

            holder.item_image = layout.findViewById(R.id.image_transaction_item);
            holder.item_label = layout.findViewById(R.id.text_transaction_label);
            holder.item_note = layout.findViewById(R.id.text_transaction_note);
            holder.item_money_trading = layout.findViewById(R.id.text_item_money_trading);

            Transaction transaction = mItems.get(section).second.get(position);
            holder.item_label.setText(transaction.getmTransactionType());
            holder.item_note.setText(transaction.getmTransactionNote());
            holder.item_money_trading.setText(String.format("%.2f",transaction.getmMoneyTrading()));

            if (transaction.getmMoneyTrading() >= 0)
                holder.item_money_trading.setTextColor(parent.getResources().getColor(R.color.colorMoneyTradingPositive));
            else holder.item_money_trading.setTextColor(parent.getResources().getColor(R.color.colorMoneyTradingNegative));
        } else {
            layout = (RelativeLayout) convertView;
        }
        return layout;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        RelativeLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (RelativeLayout) inflator.inflate(R.layout.snippet_transaction_header, null);

            HeaderViewHolder holder = new HeaderViewHolder();

            holder.header_date = layout.findViewById(R.id.text_date_header);
            holder.header_day = layout.findViewById(R.id.text_day_header);
            holder.header_month_year = layout.findViewById(R.id.text_month_year_header);
            holder.header_money_trading = layout.findViewById(R.id.text_money_trading);
            Date date = mItems.get(section).first;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
            holder.header_date.setText(simpleDateFormat.format(date));

            simpleDateFormat = new SimpleDateFormat("EEEE");
            holder.header_day.setText(simpleDateFormat.format(date));
            simpleDateFormat = new SimpleDateFormat("MM/yyyy");
            holder.header_month_year.setText(simpleDateFormat.format(date));

            float moneyTrading = 0;
            List<Transaction> transactions = mItems.get(section).second;
            for(Transaction tran : transactions) {
                moneyTrading += tran.getmMoneyTrading();
            }

            holder.header_money_trading.setText(String.format("%.2f",moneyTrading));

            if (moneyTrading >= 0)
                holder.header_money_trading.setTextColor(parent.getResources().getColor(R.color.colorMoneyTradingPositive));
            else holder.header_money_trading.setTextColor(parent.getResources().getColor(R.color.colorMoneyTradingNegative));

        } else {
            layout = (RelativeLayout) convertView;
        }
        return layout;
    }

    public static class HeaderViewHolder {
        public TextView header_date;
        public TextView header_day;
        public TextView header_month_year;
        public TextView header_money_trading;

    }

    public static class ItemViewHolder {
        public ImageView item_image;
        public TextView item_label;
        public TextView item_note;
        public TextView item_money_trading;
    }

    public static class AddTransaction extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_transaction);

            addControls();
            addEvents();
        }

        private void addControls() {
            ImageView image_money_type = (ImageView)findViewById(R.id.image_money_type);

        }

        private void addEvents() {

        }
    }
}
