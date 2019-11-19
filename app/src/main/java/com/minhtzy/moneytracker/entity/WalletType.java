package com.minhtzy.moneytracker.entity;

import java.io.Serializable;

public enum  WalletType implements Serializable {
    BASIC_WALLET("Basic"),LINKER_WALLET("LINKER");

    WalletType(String type) {
        this.value = type;
    }

    private String value;
    public String getValue()
    {
        return value;
    }

    public static WalletType from(String value)
    {
        for(WalletType type : WalletType.values()) {
            if(type.value.equals(value)) return type;
        }
        return null;
    }
}
