package lans.hotels.domain.utils;
import lans.hotels.domain.IValueObject;

public class District implements IValueObject {
    String name;

    public District(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        if (other.getClass() != District.class) return false;
        boolean sameName = this.name == ((District) other).name;
        return sameName;
    }
}