package com.minhtzy.moneytracker.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.entity.CategoryEntity;
import com.minhtzy.moneytracker.model.CategoryExpandableGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<CategoryExpandableGroup> mExpandableListDetail;

    public CategoryExpandableListAdapter(Context context,List<CategoryExpandableGroup> categories) {
        mContext = context;
        mExpandableListDetail = new ArrayList<>();
        mExpandableListDetail.addAll(categories);
    }


    @Override
    public int getGroupCount() {
        return mExpandableListDetail.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mExpandableListDetail.get(groupPosition).getSubCatCount();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mExpandableListDetail.get(groupPosition).getCategory();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mExpandableListDetail.get(groupPosition).getSubCategories().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        CategoryEntity category = (CategoryEntity) getGroup(groupPosition);

        if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.custom_item_category, null);
        }

        ImageView imganhItem = (ImageView)convertView.findViewById(R.id.imgCategoryLogo);
        TextView txtTenitem = (TextView)convertView.findViewById(R.id.txtCategoryTitle);

        txtTenitem.setText(category.getCategoryName());

        // lấy ảnh từ asset
        String base_path = "category/";
        try{
            Drawable img = Drawable.createFromStream(mContext.getAssets().open(base_path + category.getCategoryIcon()),null);
            imganhItem.setImageDrawable(img);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        CategoryEntity category = (CategoryEntity) getChild(groupPosition,childPosition);

        if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.custom_item_category, null);
        }
        convertView.setPadding(30,0,0,0);

        ImageView imganhItem = (ImageView)convertView.findViewById(R.id.imgCategoryLogo);
        TextView txtTenitem = (TextView)convertView.findViewById(R.id.txtCategoryTitle);

        txtTenitem.setText(category.getCategoryName());

        // set lại size cho ảnh
        //imganhItem.setLayoutParams(new RelativeLayout.LayoutParams(R.dimen.item_category_child_size,R.dimen.item_category_child_size));
        // lấy ảnh từ asset
        String base_path = "category/";
        try {
            Drawable img = Drawable.createFromStream(mContext.getAssets().open(base_path + category.getCategoryIcon()),null);
            imganhItem.setImageDrawable(img);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
