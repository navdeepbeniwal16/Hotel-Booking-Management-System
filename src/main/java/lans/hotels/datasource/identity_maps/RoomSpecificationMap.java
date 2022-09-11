package lans.hotels.datasource.identity_maps;

import lans.hotels.datasource.IIdentityMap;
import lans.hotels.domain.room.RoomSpecification;

public class RoomSpecificationMap implements IIdentityMap<Integer, RoomSpecification> {

    @Override
    public void add(RoomSpecification obj) {

    }

    @Override
    public RoomSpecification get(Integer id) {
        return null;
    }
}