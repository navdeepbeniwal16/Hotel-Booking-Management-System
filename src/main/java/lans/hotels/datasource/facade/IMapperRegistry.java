package lans.hotels.datasource.facade;


import lans.hotels.domain.AbstractDomainObject;

public interface IMapperRegistry<IdType> {
    IDataMapper<IdType, AbstractDomainObject<IdType>> getMapper(Class<?> aClass);
}
