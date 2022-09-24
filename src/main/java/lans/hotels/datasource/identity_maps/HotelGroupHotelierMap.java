package lans.hotels.datasource.identity_maps;

import lans.hotels.datasource.exceptions.IdentityMapException;
import lans.hotels.datasource.facade.IIdentityMap;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.hotel_group.HotelGroup;
import lans.hotels.domain.hotel_group.HotelGroupHotelier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HotelGroupHotelierMap implements IIdentityMap<HotelGroupHotelier> {
    Map<Integer, HotelGroupHotelier> hotelGroupHoteliers;

    public HotelGroupHotelierMap(Map<Integer, HotelGroupHotelier> hotelGrouHoteliers) {
        this.hotelGroupHoteliers = hotelGrouHoteliers;
    }

    @Override
    public void add(AbstractDomainObject hotelGroupHotelier) throws IdentityMapException {
        if (hotelGroupHotelier.getClass()==HotelGroupHotelier.class && hotelGroupHotelier.hasId()) {
            hotelGroupHoteliers.put(hotelGroupHotelier.getId(), (HotelGroupHotelier) hotelGroupHotelier);
        } else {
            throw new IdentityMapException("attempted to add object without an ID to identity map");
        }
    }

    @Override
    public HotelGroupHotelier getById(Integer id) {
        return hotelGroupHoteliers.get(id);
    }

    @Override
    public HotelGroupHotelier remove(Integer id) {
        return hotelGroupHoteliers.remove(id);
    }

    @Override
    public List<HotelGroupHotelier> findAll() {
        return new ArrayList<>(hotelGroupHoteliers.values());
    }

    @Override
    public void clear() {
        hotelGroupHoteliers.clear();
    }
}
