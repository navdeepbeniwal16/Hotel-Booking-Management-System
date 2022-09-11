package lans.hotels.domain.booking;

import lans.hotels.domain.ValueObject;
import lans.hotels.domain.utils.Name;

public class Guest extends ValueObject {
    Name name;

    public Guest(Name name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object other) {
        if (other.getClass() != Guest.class) return false;
        return this.name == ((Guest) other).name;
    }
}
