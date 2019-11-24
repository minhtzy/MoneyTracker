package com.minhtzy.moneytracker.model;

public enum EventStatus {
     START("START"),END("ENDED");

     private String value;
     EventStatus(String value)
     {
         this.value = value;
     }

    public String getValue()
    {
        return value;
    }

    public static EventStatus from(String value)
    {
        for(EventStatus type : EventStatus.values()) {
            if(type.value.equals(value)) return type;
        }
        return EventStatus.START;
    }
}
