package com.minhtzy.moneytracker.entity;

public class SplitTransactionsEntity extends EntityBase {
    public static final String ST_ID = "_id";
    public static final String TRAN_ID = "tranId";
    public static final String CAT_ID = "cateId";
    public static final String AMOUNT = "amount";

    public int getStId() {
        return getInt(ST_ID);
    }

    public int getTranId() {
        return getInt(TRAN_ID);
    }

    public int getCatId() {
        return getInt(CAT_ID);
    }

    public double getAmount() {
        return getDouble(AMOUNT);
    }
}
