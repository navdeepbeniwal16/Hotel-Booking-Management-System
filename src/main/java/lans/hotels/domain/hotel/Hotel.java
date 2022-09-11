package lans.hotels.domain.hotel;

import lans.hotels.domain.DomainObject;
import lans.hotels.domain.room.Room;
import lans.hotels.domain.room.RoomSpecification;
import lans.hotels.domain.utils.Phone;

import java.util.HashMap;

public class Hotel extends DomainObject {
    private String name;
    Phone phone;
    String email; // TODO: refactor email into value object.

    HashMap<Integer, Room> rooms;
    HashMap<Integer, RoomSpecification> roomSpecifications;

    public Hotel(int id) {
        super(id);
        initMaps();
    }

    protected Hotel(int id, String name, Phone phone, String email) {
        super(id);
        this.name = name;
        this.phone = phone;
        this.email = email;
        initMaps();
    }

    private void initMaps() {
        this.rooms = new HashMap<>();
        this.roomSpecifications = new HashMap<>();
    }
    public String getName() {
        return name;
    }
}
