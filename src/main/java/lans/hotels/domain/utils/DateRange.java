package lans.hotels.domain.utils;

import lans.hotels.domain.IValueObject;

import java.sql.Date;

public class DateRange implements IValueObject {
    Date from;
    Date to;

    public DateRange(Date from, Date to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean equals(Object other) {
        return true;
    }

    public Date getFrom() {
        return from;
    }

    public Date getTo() {
        return to;
    }
}
