package lans.hotels.datasource.facade;

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
