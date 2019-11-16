package com.minhtzy.moneytracker.view;

public interface ItemClickListener<T> {
    void onClickItem(int position, T item);
}
