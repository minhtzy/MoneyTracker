package com.minhtzy.moneytracker.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.common.Constants;
import com.minhtzy.moneytracker.entity.CurrencyFormat;
import com.minhtzy.moneytracker.model.Wallet;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;

public class CurrencyFormatAdapter extends ArrayAdapter<CurrencyFormat> {
    Activity context;
    int resource;
    public List<CurrencyFormat> objects;

    public CurrencyFormatAdapter(@NonNull Activity context, int resource, @NonNull List<CurrencyFormat> objects) {
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
        ImageView imgLogo = (ImageView) convertView.findViewById(R.id.imgCurrency);
        TextView txtName = (TextView) convertView.findViewById(R.id.txtCurrencyName);
        TextView txtSymbol = convertView.findViewById(R.id.txtCurrencySymbol);

        CurrencyFormat currencyFormat = this.objects.get(position);
        txtName.setText(currencyFormat.getCurrencyName());
        txtSymbol.setText(String.format("%s - %s",currencyFormat.getPfxSymbol(),currencyFormat.getCurrencySymbol()));


        // lấy ảnh từ asset
        String base_path = "currency/";
        try {
            Drawable img = Drawable.createFromStream(context.getAssets().open(String.format("%sic_currency_%s.png",base_path,currencyFormat.getCurrencySymbol().toLowerCase())), null);
            imgLogo.setImageDrawable(img);
        } catch (IOException e) {
            imgLogo.setImageResource(R.drawable.ic_currency);
            e.printStackTrace();
        }

        return convertView;
    }
}
