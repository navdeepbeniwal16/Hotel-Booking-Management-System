package lans.hotels.datasource.mappers;

import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.hotel.Hotel;
import lans.hotels.domain.room.Room;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class PostgresMapperRegistry implements IMapperRegistry<Integer> {
    Map<String, AbstractPostgresMapper> mappers;

    public static PostgresMapperRegistry getInstance(Map<String, AbstractPostgresMapper> map,
                                                     Connection connection,
                                                     IDataSource dataSource) {
        PostgresMapperRegistry registry = new PostgresMapperRegistry(map);
        registry.addMapper(new RoomMapper(connection, dataSource), Room.class);
        registry.addMapper(new HotelMapper(connection, dataSource), Hotel.class);
        return registry;
    }

    private PostgresMapperRegistry(Map<String, AbstractPostgresMapper> mappers) {
        this.mappers = mappers;
    }

    @Override
    public AbstractPostgresMapper getMapper(Class<?> aClass) {
        return mappers.get(aClass.getName());
    }

    private void addMapper(AbstractPostgresMapper mapper, Class<?> aClass) {
        mappers.put(aClass.getName());
    }
}
