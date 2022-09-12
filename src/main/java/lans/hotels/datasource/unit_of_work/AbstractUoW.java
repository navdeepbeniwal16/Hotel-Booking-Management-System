package lans.hotels.datasource.unit_of_work;

import lans.hotels.datasource.identity_maps.AbstractIdentityMapRegistry;
import lans.hotels.domain.AbstractDomainObject;

public abstract class AbstractUoW<IdType> {
    private AbstractIdentityMapRegistry<IdType> identityMaps;
    public abstract void registerNew(AbstractDomainObject obj);
    public abstract void registerDirty(AbstractDomainObject obj);
    public abstract void registerRemoved(AbstractDomainObject obj);
    public abstract void registerClean(AbstractDomainObject obj);

    public AbstractUoW(AbstractIdentityMapRegistry<IdType> identityMaps) {
        this.identityMaps = identityMaps;
    }
}
