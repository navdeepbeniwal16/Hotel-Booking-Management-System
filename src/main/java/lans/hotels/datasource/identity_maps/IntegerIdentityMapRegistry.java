package lans.hotels.datasource.identity_maps;

import lans.hotels.datasource.facade.IIdentityMap;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.room.Room;
import lans.hotels.domain.room.RoomSpecification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class IntegerIdentityMapRegistry extends AbstractIdentityMapRegistry<Integer> {
    public static IntegerIdentityMapRegistry newInstance(IDataSource<Integer> dataSource) {
        IntegerIdentityMapRegistry newRegistry = new IntegerIdentityMapRegistry(new HashMap<>(), dataSource);
        newRegistry.add(new HotelMap(new HashMap<>()), Hotel.class);
        newRegistry.add(new RoomMap(new HashMap<>()), Room.class);
        newRegistry.add(new RoomSpecificationMap(new HashMap<>()), RoomSpecification.class);
        return newRegistry;
    }

    private IntegerIdentityMapRegistry(Map<String,
            IIdentityMap<Integer, ? extends AbstractDomainObject>> identityMaps,
                                       IDataSource<Integer> dataSource) {
        super(identityMaps, dataSource);
    }

    @Override
    public ArrayList<IIdentityMap<Integer, ? extends AbstractDomainObject>> getAll() {
        return new ArrayList<>(identityMaps.values());
    }
}
