package com.example.t2m.moneytracker.model;

public class DateRange {
    private MTDate dateFrom;
    private MTDate dateTo;

    public DateRange(MTDate dateFrom, MTDate dateTo) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public MTDate getDateFrom() {
        return dateFrom;
    }

    public MTDate getDateTo() {
        return dateTo;
    }

}
