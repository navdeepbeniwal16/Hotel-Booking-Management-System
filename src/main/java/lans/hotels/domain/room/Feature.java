package lans.hotels.domain.room;

import lans.hotels.domain.IValueObject;


public class Feature implements IValueObject {
    String name;
    String description;

    public Feature(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Feature updateDescription(String newDescription) {
        return new Feature(this.name, newDescription);
    }

    @Override
    public boolean equals(Object other) {
        if (other.getClass() != Feature.class) return false;
        return this.name == ((Feature) other).name;
    }

    String getName() { return this.name; }
    String getDescription() { return this.description; }
}
