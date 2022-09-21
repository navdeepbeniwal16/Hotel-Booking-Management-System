package lans.hotels.datasource.identity_maps;

import lans.hotels.datasource.exceptions.IdentityMapException;
import lans.hotels.datasource.facade.IIdentityMap;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.hotel_group.HotelGroup;

import java.util.ArrayList;
import java.util.Map;

public class HotelGroupMap implements IIdentityMap<HotelGroup> {

    Map<Integer, HotelGroup> hotelgroups;

    public HotelGroupMap(Map<Integer, HotelGroup> hotelgroups) {
        this.hotelgroups = hotelgroups;
    }

    @Override
    public void add(AbstractDomainObject hotelgroup) throws IdentityMapException {
        if (hotelgroup.getClass()==HotelGroup.class && hotelgroup.hasId()) {
            hotelgroups.put(hotelgroup.getId(), (HotelGroup) hotelgroup);
        } else {
            throw new IdentityMapException("attempted to add object without an ID to identity map");
        }
    }

    @Override
    public HotelGroup getById(Integer id) {
        return hotelgroups.get(id);
    }

    @Override
    public HotelGroup remove(Integer id) {
        return hotelgroups.remove(id);
    }

    @Override
    public ArrayList<HotelGroup> findAll() {
        return null;
    }

    @Override
    public void clear() {
        hotelgroups.clear();
    }
}
