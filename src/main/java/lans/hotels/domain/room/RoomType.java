package lans.hotels.domain.room;

import lans.hotels.domain.ValueObject;

public class RoomType extends ValueObject {
    String name;

    public RoomType(String name) {
        super();
        this.name = name;
    }

    public RoomType(Integer id, String name) {
        super(id);
        this.name = name;
    }
    @Override
    public boolean equals(Object other) {
        if (other.getClass() != RoomType.class) return false;
        return this.getId() == ((RoomType) other).getId() || this.name == ((RoomType) other).name;
    }
}
