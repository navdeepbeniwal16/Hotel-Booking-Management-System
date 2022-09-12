package lans.hotels.domain;

public interface IDataSource<IdType> {
    void load(AbstractDomainObject domainObject);
    AbstractDomainObject find(Class<? extends AbstractDomainObject> aClass, IdType id);
    void registerNew(AbstractDomainObject domainObject);
    void registerDirty(AbstractDomainObject domainObject);
    void registerRemoved(AbstractDomainObject domainObject);
    void registerClean(AbstractDomainObject domainObject);
}
