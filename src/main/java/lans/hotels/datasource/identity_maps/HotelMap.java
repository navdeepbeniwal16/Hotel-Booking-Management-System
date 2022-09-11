package lans.hotels.datasource.identity_maps;

import lans.hotels.datasource.IIdentityMap;
import lans.hotels.domain.IReferenceObject;
import lans.hotels.domain.hotel.Hotel;

import java.util.Map;

public class HotelMap implements IIdentityMap<Integer, Hotel> {

    Map<Integer, Hotel> hotels;

    public HotelMap(Map<Integer, Hotel> hotels) {
        this.hotels = hotels;
    }

    @Override
    public void add(Hotel hotel) {
        hotels.put(hotel.getUid(), hotel);
    }

    @Override
    public Hotel get(Integer id) {
        return hotels.get(id);
    }
}
