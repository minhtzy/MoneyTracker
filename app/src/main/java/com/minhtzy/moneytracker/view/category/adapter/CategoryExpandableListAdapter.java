package com.minhtzy.moneytracker.view.category.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.entity.CategoryEntity;
import com.minhtzy.moneytracker.model.CategoryExpandableGroup;
import com.minhtzy.moneytracker.utilities.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

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
        imganhItem.setImageDrawable(ResourceUtils.getCategoryIcon(category.getCategoryIcon()));

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

        imganhItem.setImageDrawable(ResourceUtils.getCategoryIcon(category.getCategoryIcon()));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
