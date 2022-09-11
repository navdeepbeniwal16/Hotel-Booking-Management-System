package lans.hotels.domain.booking;

import lans.hotels.domain.AbstractReferenceObject;

public class Booking extends AbstractReferenceObject  {
    DateRange dateRange;

    public Booking(DateRange dateRange) {
        this.dateRange = dateRange;
    }

}
