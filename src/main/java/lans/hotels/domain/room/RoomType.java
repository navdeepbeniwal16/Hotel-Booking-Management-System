package lans.hotels.domain.room;

import lans.hotels.domain.DomainValueObject;
import lans.hotels.domain.IDataSource;

public class RoomType extends DomainValueObject<Integer> {
    String name;

    public RoomType(String name, IDataSource dataSource) {
        super(dataSource);
        this.name = name;
    }

    public RoomType(String name, Integer id, IDataSource dataSource) {
        super(id, dataSource);
        this.name = name;
    }

    @Override
    public boolean equals(Object other) {
        if (other.getClass() != RoomType.class) return false;
        return this.getId() == ((RoomType) other).getId() || this.name == ((RoomType) other).name;
    }
}
