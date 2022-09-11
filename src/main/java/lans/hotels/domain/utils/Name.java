package lans.hotels.domain.utils;

import lans.hotels.domain.IValueObject;
import lans.hotels.domain.booking.DateRange;

public class Name implements IValueObject {
    String title;
    String firstName;
    String lastName;

    public static Name build() {
        return new Name();
    }

    private Name() {
        title = "";
        firstName = "";
        lastName = "";
    }

    private Name(String title, String firstName, String lastName) {
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Name title(String title) {
        return new Name(title, this.firstName, this.lastName);
    }

    public Name firstName(String firstName) {
        return new Name(this.title, firstName, this.lastName);
    }

    public Name lastName(String lastName) {
        return new Name(this.title, this.firstName, lastName);
    }

    @Override
    public boolean equals(Object other) {
        if (other.getClass() != Name.class) return false;
        boolean sameTitle = ((Name) other).title == this.title;
        boolean sameFirstName = ((Name) other).firstName == this.firstName;
        boolean sameLastName = ((Name) other).lastName == this.lastName;
        return sameTitle && sameFirstName && sameLastName;
    }
}
