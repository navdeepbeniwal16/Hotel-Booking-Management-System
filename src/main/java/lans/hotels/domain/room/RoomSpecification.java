package lans.hotels.domain.room;

import lans.hotels.domain.DomainObject;

import java.util.HashMap;
import java.util.Map;

public class RoomSpecification extends DomainObject {
    String roomType;
    int capacity;
    String description;
    HashMap<String, Feature> features;

    public RoomSpecification(int id) {
        super(id);
    }
    private RoomSpecification(int id, String roomType, int capacity, String description) {
        super(id);
        this.roomType = roomType;
        this.capacity = capacity;
        this.description = description;
        this.features = new HashMap<>();
    }

    private RoomSpecification(int id, String roomType, int capacity, String description, Map<String, Feature> features) {
        super(id);
        this.roomType = roomType;
        this.capacity = capacity;
        this.description = description;
        this.features = new HashMap<>();
    }

    // TODO: warning - this may cause concurrency issues
    public void addFeature(Feature feature) {
        this.features.put(feature.getName(), feature);
    }
}
