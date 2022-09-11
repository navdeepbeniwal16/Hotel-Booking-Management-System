package lans.hotels.domain;

public abstract class IDomainObject<Id> {
    public abstract Id getId();
    boolean isNew();
    boolean equals(Object other);
}
