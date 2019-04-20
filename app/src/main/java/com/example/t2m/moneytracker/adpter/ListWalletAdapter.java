package com.example.t2m.moneytracker.adpter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.t2m.moneytracker.R;
import com.example.t2m.moneytracker.model.TransactionType;
import com.example.t2m.moneytracker.model.Wallet;

import java.util.List;

public class ListWalletAdapter extends ArrayAdapter<Wallet> {
    Activity context;
    int resource;
    List<Wallet> objects;

    public ListWalletAdapter(Activity context, int resource, List<Wallet> objects) {
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
        ImageView imganhItem = (ImageView) convertView.findViewById(R.id.imganhItem);
        TextView txtTenitem = (TextView) convertView.findViewById(R.id.txtTenitem);

        Wallet wallet = this.objects.get(position);
        txtTenitem.setText(wallet.getWalletName());

        // lấy id của ảnh
        int idImg = context.getResources().getIdentifier(
                wallet.getImageSrc(),
                "drawable",
                "com.example.t2m.moneytracker"
        );
        imganhItem.setImageResource(idImg);
        return convertView;
    }
}
