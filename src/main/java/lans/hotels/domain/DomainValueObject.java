package lans.hotels.domain;

public abstract class DomainValueObject<Id> extends AbstractDomainObject<Id> implements IValueObject {
    protected abstract DomainValueObject<Id> setId(Id id);

    protected DomainValueObject(IDataSource dataSource) {
        super(true, dataSource);
    }

    protected DomainValueObject(Id id, IDataSource dataSource) {
        super(id, dataSource);
    }
}
