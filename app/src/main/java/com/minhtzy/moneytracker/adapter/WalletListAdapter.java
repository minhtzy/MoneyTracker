package com.minhtzy.moneytracker.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.common.Constants;
import com.minhtzy.moneytracker.model.Wallet;

import java.util.List;

public class WalletListAdapter extends ArrayAdapter<Wallet> {
    Activity context;
    int resource;
    public List<Wallet> objects;

    public WalletListAdapter(Activity context, int resource, List<Wallet> objects) {
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

        Wallet wallet = this.objects.get(position);
        txtTenitem.setText(wallet.getWalletName());
        txtBalacne.setText(String.format(Constants.PRICE_FORMAT,wallet.getCurrentBalance()));
        if(wallet.getCurrentBalance() >= 0) {
            txtBalacne.setTextColor(getContext().getResources().getColor(R.color.colorMoneyTradingPositive));
        }
        else {
            txtBalacne.setTextColor(getContext().getResources().getColor(R.color.colorMoneyTradingNegative));
        }

        // lấy id của ảnh
        int idImg = context.getResources().getIdentifier(
                wallet.getImageSrc(),
                "drawable",
                "com.minhtzy.moneytracker"
        );
        if(idImg == 0) {
            idImg = R.drawable.ic_account_balance_wallet_black_24dp;
        }
        imganhItem.setImageResource(idImg);
//         lấy ảnh từ asset
//        String base_path = "category/";
//        try {
//            Drawable img = Drawable.createFromStream(getContext().getAssets().open(base_path + wallet.getImageSrc()),null);
//            imganhItem.setImageDrawable(img);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return convertView;
    }
}
