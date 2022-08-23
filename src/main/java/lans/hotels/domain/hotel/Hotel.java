package lans.hotels.domain.hotel;

import lans.hotels.domain.Entity;

import java.util.UUID;

public class Hotel extends Entity {
    private String name;
    public Hotel(UUID UUID, String name) {
        super.uuid = UUID;
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return this.uuid;
    }
}
