package lans.hotels.domain.exceptions;

import lans.hotels.domain.DomainValueObject;

public class DomainObjectException extends Exception {
    public DomainObjectException(String message) {
        super(message);
    }
}
