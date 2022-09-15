package lans.hotels.datasource.identity_maps;

import lans.hotels.datasource.facade.IIdentityMap;
import lans.hotels.domain.hotel.Hotel;

import java.util.Map;

public class HotelMap implements IIdentityMap<Integer, Hotel> {

    Map<Integer, Hotel> hotels;

    public HotelMap(Map<Integer, Hotel> hotels) {
        this.hotels = hotels;
    }

    @Override
    public void add(Hotel hotel) {
        hotels.put(hotel.getId(), hotel);
    }

    @Override
    public Hotel getById(Integer id) {
        return hotels.get(id);
    }
}
