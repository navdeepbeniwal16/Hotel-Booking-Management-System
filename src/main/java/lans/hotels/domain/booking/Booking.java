package lans.hotels.domain.booking;

import lans.hotels.domain.AbstractReferenceObject;
import lans.hotels.domain.utils.DateRange;

public class Booking extends AbstractReferenceObject  {
    DateRange dateRange;

    public Booking(DateRange dateRange) {
        this.dateRange = dateRange;
    }

}
