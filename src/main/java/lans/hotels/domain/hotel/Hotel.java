package lans.hotels.domain.hotel;

import lans.hotels.domain.EntityI;

import java.util.UUID;

public class Hotel implements EntityI {
    private String name;
    private UUID uuid;
    public Hotel(Integer UUID, String name) {
        this.name = name;
        this.UUID = UUID;
    }
    public String getName() {
        return name;
    }

    public boolean is(Hotel other) {
        return isSameEntity(other);
    }

    @Override
    public java.util.UUID getUUID() {
        return null;
    }

    @Override
    public boolean isSameEntity(EntityI otherEntity) {
        return this.uuid == otherEntity.getUUID();
    }
}
