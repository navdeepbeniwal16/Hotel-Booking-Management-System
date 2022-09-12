package lans.hotels.datasource.mappers;

import lans.hotels.domain.AbstractDomainObject;

import java.util.Map;

public class PostgresMapperRegistry implements IMapperRegistry<Integer> {
    Map<String, AbstractPostgresMapper> mappers;

    public PostgresMapperRegistry(Map<String, AbstractPostgresMapper> mappers) {
        this.mappers = mappers;
    }

    @Override
    public AbstractPostgresMapper getMapper(Class<?> aClass) {
        return mappers.get(aClass.getName());
    }
}
