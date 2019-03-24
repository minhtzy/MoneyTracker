package com.example.t2m.moneytracker.adpter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.t2m.moneytracker.R;
import com.example.t2m.moneytracker.model.ListChoose;

import java.util.List;

/**
 * Created by minhthuanht on 24,March,2019
 */
public class AdapterListChoose extends ArrayAdapter<ListChoose> {
    Activity context;
    int resource;
    List<ListChoose> objects;
    public AdapterListChoose(Activity context, int resource, List<ListChoose> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }


    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View row = inflater.inflate(this.resource,null);
        ImageView imganhItem = (ImageView)row.findViewById(R.id.imganhItem);
        TextView txtTenitem = (TextView)row.findViewById(R.id.txtTenitem);

        ListChoose listChoose = this.objects.get(position);
        txtTenitem.setText(listChoose.getTen());

        // lấy id của ảnh
        int idImg = context.getResources().getIdentifier(
                listChoose.getAnh(),
                "drawable",
                context.getPackageName()
        );
        imganhItem.setImageResource(idImg);
        return row;
    }
}
