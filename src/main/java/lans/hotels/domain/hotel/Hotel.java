package lans.hotels.domain.hotel;

import lans.hotels.domain.AbstractReferenceObject;

public class Hotel extends AbstractReferenceObject {
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
