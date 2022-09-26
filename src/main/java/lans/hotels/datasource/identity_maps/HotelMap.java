package lans.hotels.datasource.identity_maps;

import lans.hotels.datasource.exceptions.IdentityMapException;
import lans.hotels.datasource.facade.IIdentityMap;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.hotel.Hotel;

import java.util.ArrayList;
import java.util.Map;

public class HotelMap implements IIdentityMap<Hotel> {

    Map<Integer, Hotel> hotels;

    public HotelMap(Map<Integer, Hotel> hotels) {
        this.hotels = hotels;
    }

    @Override
    public void add(AbstractDomainObject hotel) throws IdentityMapException {}

    @Override
    public Hotel getById(Integer id) {
        return hotels.get(id);
    }

    @Override
    public Hotel remove(Integer id) {
        return hotels.remove(id);
    }

    @Override
    public ArrayList<Hotel> findAll() {
        return null;
    }

    @Override
    public void clear() {
        hotels.clear();
    }
}
