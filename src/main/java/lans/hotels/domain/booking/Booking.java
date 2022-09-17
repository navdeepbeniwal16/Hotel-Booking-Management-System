package lans.hotels.domain.booking;

import lans.hotels.domain.ReferenceObject;
import lans.hotels.domain.utils.DateRange;

public class Booking {
    DateRange dateRange;

    public Booking(DateRange dateRange) {
        this.dateRange = dateRange;
    }

}
