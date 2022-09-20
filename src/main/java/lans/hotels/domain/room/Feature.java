package lans.hotels.domain.room;

import lans.hotels.domain.DomainValueObject;
import lans.hotels.domain.IDataSource;


public class Feature extends DomainValueObject {
    String name;
    String description;

    public Feature(String name, String description, IDataSource dataSource) {
        super(dataSource);
        this.name = name;
        this.description = description;
    }

    public Feature(String name, String description, Integer id, IDataSource dataSource) {
        super(id, dataSource);
        this.name = name;
        this.description = description;
    }


    String getName() { return this.name; }
    String getDescription() { return this.description; }

    // TODO: authorisation - only admin?
    public Feature setDescription(String newDescription) {
        return new Feature(name, newDescription, id, dataSource);
    }

    @Override
    public boolean equals(Object other) {
        if (other.getClass() != Feature.class) return false;
        return this.name == ((Feature) other).name;
    }
}
