package lans.hotels.domain;

public interface IValueObject<T extends AbstractDomainObject> {
    boolean equals(Object other);
}
