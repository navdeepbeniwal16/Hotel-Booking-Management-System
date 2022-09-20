package lans.hotels.datasource.mappers;

import lans.hotels.datasource.exceptions.MapperNotFoundException;
import lans.hotels.datasource.facade.IDataMapper;
import lans.hotels.datasource.facade.IMapperRegistry;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.user_types.Customer;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.room.Room;
import lans.hotels.domain.user_types.Hotelier;
import lans.hotels.domain.user_types.User;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class PostgresMapperRegistry implements IMapperRegistry<Integer> {
    Map<String, AbstractPostgresDataMapper> mappers;

    public static PostgresMapperRegistry newInstance(Connection connection, IDataSource dataSource) {
        PostgresMapperRegistry registry = new PostgresMapperRegistry(new HashMap<>());

        registry.addMapper(Room.class, new RoomDataMapper(connection, dataSource));
        registry.addMapper(Hotel.class, new HotelDataMapper(connection, dataSource));
        registry.addMapper(Customer.class, new CustomerDataMapper(connection, dataSource));
        registry.addMapper(Hotelier.class, new HotelierDataMapper(connection, dataSource));
        registry.addMapper(User.class, new UserDataMapper(connection, dataSource));

        return registry;
    }

    private PostgresMapperRegistry(Map<String, AbstractPostgresDataMapper> mappers) {
        this.mappers = mappers;
    }

    @Override
    public AbstractPostgresDataMapper getMapper(Class<? extends AbstractDomainObject> aClass) throws MapperNotFoundException {
        AbstractPostgresDataMapper mapper = mappers.get(aClass.getName());
        if (mapper==null) throw new MapperNotFoundException("PostgresMapperRegistry: class not found - " + aClass.getName());
        return mapper;
    }

    private void addMapper(Class<?> aClass,AbstractPostgresDataMapper mapper) {
        mappers.put(aClass.getName(), mapper);
    }
}
