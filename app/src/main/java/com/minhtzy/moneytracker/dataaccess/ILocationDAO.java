package com.minhtzy.moneytracker.dataaccess;

import com.minhtzy.moneytracker.entity.LocationEntity;

interface ILocationDAO {
    public boolean insertLocation(LocationEntity locationEntity);
    public LocationEntity getLocationById(String id);
}
