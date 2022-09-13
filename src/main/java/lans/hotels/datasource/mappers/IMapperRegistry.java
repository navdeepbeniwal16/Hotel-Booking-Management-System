package lans.hotels.datasource.mappers;


import lans.hotels.domain.AbstractDomainObject;

public interface IMapperRegistry<IdType> {
    IMapper<IdType, AbstractDomainObject> getMapper(Class<?> aClass);
}
