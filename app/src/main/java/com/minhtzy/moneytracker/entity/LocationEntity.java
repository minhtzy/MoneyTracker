package com.minhtzy.moneytracker.entity;

public class LocationEntity extends EntityBase {
    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String COUNTRY = "country";
    public static final String CITY = "city";
    public static final String DISTRICT = "district";
    public static final String LOCALITY = "locality";
    public static final String ROUTE = "route";
    public static final String STREET_NUMBER = "streetNumber";


    public String getId()
    {
        return getString(ID);
    }

    public void setId(String id)
    {
        setString(ID,id);
    }
    public String getName()
    {
        return getString(NAME);
    }

    public void setName(String name)
    {
        setString(NAME,name);
    }
}

