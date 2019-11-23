package com.minhtzy.moneytracker.dataaccess;

import com.minhtzy.moneytracker.entity.EventEntity;

import java.util.List;

public interface IEventDAO {
    List<EventEntity> getAllAvaialbleEvent();
}
