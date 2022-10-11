package lans.hotels.datasource.identity_maps;

import lans.hotels.datasource.facade.IIdentityMap;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.booking.Booking;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.hotel_group.HotelGroup;
import lans.hotels.domain.hotel_group.HotelGroupHotelier;
import lans.hotels.domain.room.Room;
import lans.hotels.domain.user.User;

import java.util.ArrayList;
import java.util.HashMap;

public class IntegerIdentityMapRegistry extends AbstractIdentityMapRegistry {
    public static IntegerIdentityMapRegistry newInstance(IDataSource dataSource) {
        IntegerIdentityMapRegistry newRegistry = new IntegerIdentityMapRegistry(new HashMap<>(), dataSource);
        newRegistry.add(new HotelMap(new HashMap<>()), Hotel.class);
        newRegistry.add(new UserMap(new HashMap<>()), User.class);
        newRegistry.add(new BookingMap(new HashMap<>()), Booking.class);
        newRegistry.add(new RoomMap(new HashMap<>()), Room.class);
        newRegistry.add(new HotelGroupMap(new HashMap<>()), HotelGroup.class);
        newRegistry.add(new HotelGroupHotelierMap(new HashMap<>()), HotelGroupHotelier.class);
        return newRegistry;
    }

    private IntegerIdentityMapRegistry(HashMap<String,
            IIdentityMap<? extends AbstractDomainObject>> identityMaps,
                                       IDataSource dataSource) {
        super(identityMaps, dataSource);
    }

    @Override
    public ArrayList<IIdentityMap<? extends AbstractDomainObject>> getAll() {
        return new ArrayList<>(identityMaps.values());
    }
}
