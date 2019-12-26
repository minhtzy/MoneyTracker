package com.minhtzy.moneytracker.view.event;

import com.minhtzy.moneytracker.entity.EventEntity;

public interface OnEventItemInteractionListener {

    void onEventItemClicked(EventEntity entity);
}
