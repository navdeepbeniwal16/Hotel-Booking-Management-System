package lans.hotels.domain.utils;

import lans.hotels.domain.IValueObject;

import java.util.Date;

public class DateRange implements IValueObject {
    Date from;
    Date to;

    public DateRange(Date from, Date to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean equals(Object other) {
        if (other.getClass() != DateRange.class) return false;
        boolean sameFrom = ((DateRange) other).from == this.from;
        boolean sameTo = ((DateRange) other).to == this.to;
        return sameFrom && sameTo;
    }

    public Date getFrom() {
        return from;
    }

    public Date getTo() {
        return to;
    }

    public DateRange startSooner(Date newFrom) {
        if (newFrom.after(this.from)) return this;
        return new DateRange(newFrom, this.to);
    }

    public DateRange endLater(Date newTo) {
        if (newTo.before(this.to)) return this;
        return new DateRange(this.from, newTo);
    }
}
