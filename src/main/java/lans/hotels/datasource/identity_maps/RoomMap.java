package lans.hotels.datasource.identity_maps;

import lans.hotels.datasource.exceptions.IdentityMapException;
import lans.hotels.datasource.facade.IIdentityMap;
import lans.hotels.domain.room.Room;
import lans.hotels.domain.room.RoomSpecification;

import java.util.List;
import java.util.Map;

public class RoomMap implements IIdentityMap<Integer, Room> {
    Map<Integer, Room> rooms;

    public RoomMap(Map<Integer, Room> rooms) {
        this.rooms = rooms;
    }

    @Override
    public void add(Room room) throws IdentityMapException {
        if (room.hasId()) {
            rooms.put(room.getId(), room);
        } else {
            throw new IdentityMapException("attempted to add object without an ID to identity map");
        }
    }

    @Override
    public Room getById(Integer id) {
        return rooms.get(id);
    }

    @Override
    public Room remove(Integer id) {
        return rooms.remove(id);
    }

    @Override
    public List<Room> findAll() {
        return null;
    }

    @Override
    public void clear() {
        rooms.clear();
    }
}
