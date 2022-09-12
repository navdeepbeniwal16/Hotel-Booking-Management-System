package lans.hotels.datasource.mappers;


import lans.hotels.datasource.identity_maps.IIdentityMap;
import lans.hotels.domain.AbstractDomainObject;

public interface IMapperRegistry<IdType> {
    IMapper<IdType, AbstractDomainObject> getMapper(Class<?> aClass);
}
