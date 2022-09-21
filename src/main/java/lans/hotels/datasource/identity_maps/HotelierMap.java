package lans.hotels.datasource.identity_maps;

import lans.hotels.datasource.exceptions.IdentityMapException;
import lans.hotels.datasource.facade.IIdentityMap;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.user_types.Hotelier;

import java.util.ArrayList;
import java.util.Map;

public class HotelierMap implements IIdentityMap<Hotelier> {

    Map<Integer, Hotelier> hoteliers;

    public HotelierMap(Map<Integer, Hotelier> hoteliers) {
        this.hoteliers = hoteliers;
    }

    @Override
    public void add(AbstractDomainObject hoteliers) throws IdentityMapException {
        if (hoteliers.getClass()==Hotelier.class && hoteliers.hasId()) {
            this.hoteliers.put(hoteliers.getId(), (Hotelier) hoteliers);
        } else {
            throw new IdentityMapException("attempted to add object without an ID to identity map");
        }
    }

    @Override
    public Hotelier getById(Integer id) {
        return hoteliers.get(id);
    }

    @Override
    public Hotelier remove(Integer id) {
        return hoteliers.remove(id);
    }

    @Override
    public ArrayList<Hotelier> findAll() {
        return null;
    }

    @Override
    public void clear() {
        hoteliers.clear();
    }
}
