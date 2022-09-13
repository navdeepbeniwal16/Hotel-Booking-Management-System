package lans.hotels.datasource.mappers;

import lans.hotels.datasource.facade.IMapperRegistry;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.room.Room;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class PostgresMapperRegistry implements IMapperRegistry<Integer> {
    Map<String, AbstractPostgresDataMapper> mappers;

    public static PostgresMapperRegistry newInstance(Connection connection) {
        PostgresMapperRegistry registry = new PostgresMapperRegistry(new HashMap<>());
        registry.addMapper(new RoomDataMapper(connection), Room.class);
        registry.addMapper(new HotelDataMapper(connection), Hotel.class);
        return registry;
    }

    private PostgresMapperRegistry(Map<String, AbstractPostgresDataMapper> mappers) {
        this.mappers = mappers;
    }

    @Override
    public AbstractPostgresDataMapper getMapper(Class<?> aClass) {
        return mappers.get(aClass.getName());
    }

    private void addMapper(AbstractPostgresDataMapper mapper, Class<?> aClass) {
        mappers.put(aClass.getName(), mapper);
    }
}
