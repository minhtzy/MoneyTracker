package com.minhtzy.moneytracker.view.category.adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.dataaccess.CategoriesDAOImpl;
import com.minhtzy.moneytracker.model.CategoryExpandableGroup;
import com.minhtzy.moneytracker.view.wallet.ListCategoryFragment;

import java.util.ArrayList;
import java.util.List;

public class CategoriesPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    Context  mContext;

    public CategoriesPagerAdapter(FragmentManager fm,Context context){
        super(fm);
        mContext =context;
    }
    @Override
    public Fragment getItem(int i) {
        List<CategoryExpandableGroup> categories = new ArrayList<>();
        CategoriesDAOImpl categoriesDAO = new CategoriesDAOImpl(mContext);
        switch (i) {
            case 0 :
                categories.addAll(categoriesDAO.getExpandedCategoriesByType(3));
                categories.addAll(categoriesDAO.getExpandedCategoriesByType(4));
                break;
            case 1 :
                categories.addAll(categoriesDAO.getExpandedCategoriesByType(1));
                //categories = categories.subList(0,5);
                break;
            case 2 :
                categories.addAll(categoriesDAO.getExpandedCategoriesByType(2));

        }
        return ListCategoryFragment.newInstance(categories);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return mContext.getString(R.string.debt_loan_category_title);
            case 1: return mContext.getString(R.string.expense_category_title);
            case 2: return mContext.getString(R.string.income_category_title);
        }
        return null;
    }

}