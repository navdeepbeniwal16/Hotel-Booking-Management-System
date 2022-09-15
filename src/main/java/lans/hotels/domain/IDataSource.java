package lans.hotels.domain;

public interface IDataSource<IdType> {
    void load(AbstractDomainObject domainObject);
    <T extends AbstractDomainObject> T find(Class<T> aClass, IdType id);
    void registerNew(AbstractDomainObject domainObject);
    void registerDirty(AbstractDomainObject domainObject);
    void registerRemoved(AbstractDomainObject domainObject);
    void registerClean(AbstractDomainObject domainObject);
}
