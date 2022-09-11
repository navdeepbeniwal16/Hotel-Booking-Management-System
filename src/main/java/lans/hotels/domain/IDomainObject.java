package lans.hotels.domain;

public interface IDomainObject<Id> {
    Id getId();
    boolean equals(Object other);
}
