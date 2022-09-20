package lans.hotels.datasource.identity_maps;

import lans.hotels.datasource.exceptions.IdentityMapException;
import lans.hotels.datasource.facade.IIdentityMap;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.room.RoomSpecification;

import java.util.List;
import java.util.Map;

public class RoomSpecificationMap implements IIdentityMap<RoomSpecification> {

    Map<Integer, RoomSpecification> specifications;

    public RoomSpecificationMap(Map<Integer, RoomSpecification> specifications) {
        this.specifications = specifications;
    }

    @Override
    public void add(AbstractDomainObject spec) throws IdentityMapException {
        if (spec.getClass()==RoomSpecification.class && spec.hasId()) {
            specifications.put(spec.getId(), (RoomSpecification) spec);
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

    @Override
    public List<RoomSpecification> findAll() {
        return null;
    }

    @Override
    public void clear() {
        specifications.clear();
    }
}