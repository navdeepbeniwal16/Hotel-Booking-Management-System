package lans.hotels.datasource.mappers;

import lans.hotels.domain.AbstractDomainObject;

public interface IMapper<IdType, DomainObject extends AbstractDomainObject<IdType>> {
    DomainObject getById(IdType id);
}
