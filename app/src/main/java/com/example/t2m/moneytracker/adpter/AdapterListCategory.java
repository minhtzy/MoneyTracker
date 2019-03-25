package com.example.t2m.moneytracker.adpter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.t2m.moneytracker.R;
import com.example.t2m.moneytracker.model.TransactionType;

import java.util.List;


public class AdapterListCategory extends ArrayAdapter<TransactionType> {
    Activity context;
    int resource;
    List<TransactionType> objects;
    public AdapterListCategory(Activity context, int resource, List<TransactionType> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }


    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView == null)
            convertView= inflater.inflate(this.resource,null);
        ImageView imganhItem = (ImageView)convertView.findViewById(R.id.imganhItem);
        TextView txtTenitem = (TextView)convertView.findViewById(R.id.txtTenitem);

        TransactionType transactionType = this.objects.get(position);
        txtTenitem.setText(transactionType.getCategory());

        // lấy id của ảnh
        Log.d("AdapterListCategory",transactionType.getIcon() + " " + context.getPackageName());
        int idImg = context.getResources().getIdentifier(
                transactionType.getIcon(),
                "drawable",
                "com.example.t2m.moneytracker"
        );
        imganhItem.setImageResource(idImg);
        return convertView;
    }

}
