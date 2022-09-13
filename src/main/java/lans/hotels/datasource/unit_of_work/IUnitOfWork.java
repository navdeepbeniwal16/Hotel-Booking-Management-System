package lans.hotels.datasource.unit_of_work;

import lans.hotels.domain.AbstractDomainObject;

public interface IUnitOfWork<IdType> {
//    private AbstractIdentityMapRegistry<IdType> identityMaps;
    void registerNew(AbstractDomainObject obj);
    void registerDirty(AbstractDomainObject obj);
    void registerRemoved(AbstractDomainObject obj);
    void registerClean(AbstractDomainObject obj);

//    public AbstractUoW(AbstractIdentityMapRegistry<IdType> identityMaps) {
//        this.identityMaps = identityMaps;
//    }
}
