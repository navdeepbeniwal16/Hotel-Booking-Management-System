package lans.hotels.domain;

public abstract class AbstractDomainObject<Id> {
    public abstract Id getId();
    protected abstract boolean isNew();
    public abstract boolean equals(Object other);
}
