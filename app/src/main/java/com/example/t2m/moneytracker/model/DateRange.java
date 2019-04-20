package com.example.t2m.moneytracker.model;

import java.util.Date;

public class DateRange {
    private Date dateFrom;
    private Date dateTo;

    public DateRange(Date dateFrom, Date dateTo) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public boolean isContain(Date date) {
        MTDate dateC = new MTDate(date);
        MTDate dateF = new MTDate(dateFrom);
        MTDate dateT = new MTDate(dateTo);
        return dateC.getCalendar().compareTo(dateF.getCalendar()) >= 0 && dateC.getCalendar().compareTo(dateT.getCalendar()) <= 0;
    }
}
