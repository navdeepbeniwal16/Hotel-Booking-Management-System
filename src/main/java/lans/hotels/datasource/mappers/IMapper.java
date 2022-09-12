package lans.hotels.datasource.mappers;

import lans.hotels.domain.AbstractDomainObject;

public interface IMapper<IdType, DomainObject extends AbstractDomainObject> {
    DomainObject getById(IdType id);
}
