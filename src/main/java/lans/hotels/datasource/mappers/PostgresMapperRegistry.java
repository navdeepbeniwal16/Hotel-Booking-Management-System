package lans.hotels.datasource.mappers;

import lans.hotels.datasource.exceptions.MapperNotFoundException;
import lans.hotels.datasource.facade.IMapperRegistry;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.booking.Booking;
import lans.hotels.domain.booking.RoomBooking;
import lans.hotels.domain.hotel_group.HotelGroup;
import lans.hotels.domain.hotel_group.HotelGroupHotelier;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.room.Room;
import lans.hotels.domain.user.User;

import java.sql.Connection;
import java.util.HashMap;

public class PostgresMapperRegistry implements IMapperRegistry {
    // implements IDataMapper<Integer, >
    HashMap<String, AbstractPostgresDataMapper<? extends AbstractDomainObject>> mappers;

    public static PostgresMapperRegistry newInstance(Connection connection, IDataSource dataSource) {
        PostgresMapperRegistry registry = new PostgresMapperRegistry(new HashMap<>());

        registry.addMapper(Room.class, new RoomDataMapper(connection, dataSource));
        registry.addMapper(Hotel.class, new HotelDataMapper(connection, dataSource));
        registry.addMapper(User.class, new UserDataMapper(connection, dataSource));
        registry.addMapper(HotelGroup.class, new HotelGroupDataMapper(connection, dataSource));
        registry.addMapper(HotelGroupHotelier.class, new HotelGroupHotelierDataMapper(connection, dataSource));
        registry.addMapper(Booking.class, new BookingDataMapper(connection, dataSource));
        registry.addMapper(RoomBooking.class, new RoomBookingDataMapper(connection, dataSource));
        return registry;
    }

    private PostgresMapperRegistry(HashMap<String, AbstractPostgresDataMapper<? extends AbstractDomainObject>> mappers) {
        this.mappers = mappers;
    }

    public AbstractPostgresDataMapper getMapper(Class aClass) throws MapperNotFoundException {
        AbstractPostgresDataMapper mapper = mappers.get(aClass.getName()); // TODO: #bug - possible loss of data from type checking
        if (mapper==null) throw new MapperNotFoundException("PostgresMapperRegistry: class not found - " + aClass.getName());
        return mapper;
    }

    private <DomainObj extends AbstractDomainObject, T extends AbstractPostgresDataMapper<DomainObj>> void addMapper(Class<DomainObj> aClass, T mapper) {
        mappers.put(aClass.getName(), mapper);
    }
}
