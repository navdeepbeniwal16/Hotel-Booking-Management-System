package lans.hotels.datasource.identity_maps;

import lans.hotels.datasource.exceptions.IdentityMapException;
import lans.hotels.datasource.facade.IIdentityMap;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.booking.Booking;

import java.util.ArrayList;
import java.util.Map;

public class BookingMap implements IIdentityMap<Booking> {

    Map<Integer, Booking> bookings;

    public BookingMap(Map<Integer, Booking> bookings) {
        this.bookings = bookings;
    }

    @Override
    public void add(AbstractDomainObject bookings) throws IdentityMapException {
    }

    @Override
    public Booking getById(Integer id) {
        return bookings.get(id);
    }

    @Override
    public Booking remove(Integer id) {
        return bookings.remove(id);
    }

    @Override
    public ArrayList<Booking> findAll() {
        return null;
    }

    @Override
    public void clear() {
        bookings.clear();
    }
}
