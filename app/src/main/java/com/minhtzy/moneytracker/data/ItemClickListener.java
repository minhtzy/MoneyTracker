package com.minhtzy.moneytracker.data;

public interface ItemClickListener<T> {
    void onClickItem(int position, T item);
}
