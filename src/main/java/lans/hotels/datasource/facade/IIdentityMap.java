package lans.hotels.datasource.facade;

import lans.hotels.datasource.exceptions.IdentityMapException;
import lans.hotels.domain.AbstractDomainObject;

import java.util.List;

public interface IIdentityMap<IdType, DomainObject extends AbstractDomainObject> {
    void add(DomainObject obj) throws IdentityMapException;
    DomainObject getById(IdType id);
    DomainObject remove(IdType id);

    List<DomainObject> findAll();

    void clear();
}
