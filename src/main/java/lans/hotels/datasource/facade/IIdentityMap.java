package lans.hotels.datasource.facade;

import lans.hotels.datasource.exceptions.IdentityMapException;
import lans.hotels.domain.AbstractDomainObject;

import java.util.List;

public interface IIdentityMap<DomainObject extends AbstractDomainObject> {
    void add(AbstractDomainObject obj) throws IdentityMapException;
    DomainObject getById(Integer id);
    DomainObject remove(Integer id);

    List<DomainObject> findAll();

    void clear();
}
