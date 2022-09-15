package lans.hotels.datasource.identity_maps;

import lans.hotels.datasource.exceptions.IdentityMapException;
import lans.hotels.datasource.facade.IIdentityMap;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.room.Room;
import lans.hotels.domain.room.RoomSpecification;

import java.util.Map;

public class RoomSpecificationMap implements IIdentityMap<Integer, RoomSpecification> {

    Map<Integer, RoomSpecification> specifications;
    @Override
    public void add(RoomSpecification spec) throws IdentityMapException {
        if (spec.hasId()) {
            specifications.put(spec.getId(), spec);
        } else {
            throw new IdentityMapException("attempted to add object without an ID to identity map");
        }
    }

    @Override
    public RoomSpecification getById(Integer id) {
        return specifications.get(id);
    }

    @Override
    public RoomSpecification remove(Integer id) {
        return specifications.remove(id);
    }
}