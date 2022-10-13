package lans.hotels.datasource.identity_maps;

import lans.hotels.datasource.exceptions.IdentityMapException;
import lans.hotels.datasource.facade.IIdentityMap;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.booking.Booking;
import lans.hotels.domain.booking.RoomBooking;

import java.util.ArrayList;
import java.util.Map;

public class RoomBookingMap implements IIdentityMap<RoomBooking> {

    Map<Integer, RoomBooking> room_bookings;

    public RoomBookingMap(Map<Integer, RoomBooking> room_bookings) {
        this.room_bookings = room_bookings;
    }

    @Override
    public void add(AbstractDomainObject bookings) throws IdentityMapException {
    }

    @Override
    public RoomBooking getById(Integer id) {
        return room_bookings.get(id);
    }

    @Override
    public RoomBooking remove(Integer id) {
        return room_bookings.remove(id);
    }

    @Override
    public ArrayList<RoomBooking> findAll() {
        return null;
    }

    @Override
    public void clear() {
        room_bookings.clear();
    }
}
