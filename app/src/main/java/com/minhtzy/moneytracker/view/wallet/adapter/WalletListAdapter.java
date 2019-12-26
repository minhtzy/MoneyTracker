package com.minhtzy.moneytracker.view.wallet.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.entity.WalletEntity;
import com.minhtzy.moneytracker.utilities.CurrencyUtils;
import com.minhtzy.moneytracker.utilities.ResourceUtils;

import java.util.List;

public class WalletListAdapter extends ArrayAdapter<WalletEntity> {
    Activity context;
    int resource;
    public List<WalletEntity> objects;

    public WalletListAdapter(Activity context, int resource, List<WalletEntity> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        if (convertView == null)
            convertView = inflater.inflate(this.resource, null);
        ImageView imganhItem = (ImageView) convertView.findViewById(R.id.imgWalletLogo);
        TextView txtTenitem = (TextView) convertView.findViewById(R.id.txtWalletTitle);
        TextView txtBalacne = convertView.findViewById(R.id.txtWalletBalance);

        WalletEntity wallet = this.objects.get(position);
        txtTenitem.setText(wallet.getName());
        txtBalacne.setText(CurrencyUtils.getInstance().formatCurrency(String.valueOf(wallet.getCurrentBalance()),wallet.getCurrencyCode()));
        if(wallet.getCurrentBalance() >= 0) {
            txtBalacne.setTextColor(getContext().getResources().getColor(R.color.colorMoneyTradingPositive));
        }
        else {
            txtBalacne.setTextColor(getContext().getResources().getColor(R.color.colorMoneyTradingNegative));
        }
        imganhItem.setImageDrawable(ResourceUtils.getWalletIcon(wallet.getIcon()));
        return convertView;
    }
}
