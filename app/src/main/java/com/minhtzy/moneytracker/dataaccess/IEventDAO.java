package com.minhtzy.moneytracker.dataaccess;

import com.minhtzy.moneytracker.entity.EventEntity;
import com.minhtzy.moneytracker.model.EventStatus;

import java.util.List;

public interface IEventDAO {
    List<EventEntity> getAllAvailableEvent(EventStatus eventStatus);

    boolean insert(EventEntity entity);
}
