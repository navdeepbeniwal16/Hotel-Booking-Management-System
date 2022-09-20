package lans.hotels.datasource.facade;


import lans.hotels.datasource.exceptions.MapperNotFoundException;
import lans.hotels.datasource.mappers.AbstractPostgresDataMapper;
import lans.hotels.domain.AbstractDomainObject;

public interface IMapperRegistry {
   <DomainObject extends AbstractDomainObject> AbstractPostgresDataMapper<DomainObject> getMapper(Class<DomainObject> aClass) throws MapperNotFoundException;
}
