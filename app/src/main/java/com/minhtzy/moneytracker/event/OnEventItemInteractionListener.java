package com.minhtzy.moneytracker.event;

import com.minhtzy.moneytracker.entity.EventEntity;

public interface OnEventItemInteractionListener {

    void onEventItemClicked(EventEntity entity);
}
