package lans.hotels.domain.hotel;

import lans.hotels.domain.Entity;

import java.util.UUID;

public class Hotel extends Entity {
    private String name;

    public Hotel(int id) {
        super(id);
    }
    public Hotel(int id, String name) {
        super(id);
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
