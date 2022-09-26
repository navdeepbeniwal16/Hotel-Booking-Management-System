package lans.hotels.datasource.identity_maps;

import lans.hotels.datasource.exceptions.IdentityMapException;
import lans.hotels.datasource.facade.IIdentityMap;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.room.Room;

import java.util.List;
import java.util.Map;

public class RoomMap implements IIdentityMap<Room> {
    Map<Integer, Room> rooms;

    public RoomMap(Map<Integer, Room> rooms) {
        this.rooms = rooms;
    }

    @Override
    public void add(AbstractDomainObject room) throws IdentityMapException {
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
