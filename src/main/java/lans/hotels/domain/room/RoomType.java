package lans.hotels.domain.room;

import lans.hotels.domain.ReferenceObject;
import lans.hotels.domain.ValueObject;

public class RoomType extends ReferenceObject<Integer> implements ValueObject<Integer> {
    String name;

    public RoomType(String name) {
        this.name = name;
    }

    public RoomType(Integer id, String name) {
        super(id);
        this.name = name;
    }

    @Override
    protected Boolean isNew() {
        return null;
    }

    @Override
    public boolean equals(Object other) {
        if (other.getClass() != RoomType.class) return false;
        return this.getId() == ((RoomType) other).getId() || this.name == ((RoomType) other).name;
    }
}
