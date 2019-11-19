package com.minhtzy.moneytracker.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.entity.CategoryEntity;

import java.io.IOException;
import java.util.List;


    public class CategoryListAdapter extends ArrayAdapter<CategoryEntity> {

        Activity context;
        int resource;
        public List<CategoryEntity> objects;

        public CategoryListAdapter(Activity context, int resource, List<CategoryEntity> objects) {
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
            ImageView imganhItem = (ImageView) convertView.findViewById(R.id.imgCategoryLogo);
            TextView txtTenitem = (TextView) convertView.findViewById(R.id.txtCategoryTitle);

            CategoryEntity category = this.objects.get(position);
            txtTenitem.setText(category.getCategoryName());

            // lấy ảnh từ asset
            String base_path = "category/";
            try {
                Drawable img = Drawable.createFromStream(context.getAssets().open(base_path + category.getCategoryIcon()), null);
                imganhItem.setImageDrawable(img);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return convertView;
        }

    }

