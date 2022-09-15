package lans.hotels.domain.room;

import lans.hotels.domain.IDataSource;
import lans.hotels.domain.ReferenceObject;

import java.util.HashMap;
import java.util.Map;

public class RoomSpecification extends ReferenceObject {
    String roomType;
    int capacity;
    String description;
    HashMap<String, Feature> features;

    public RoomSpecification(IDataSource dataSource) {
        super(dataSource);
    }

    public RoomSpecification(Integer id, IDataSource dataSource) {
        super(id, dataSource);
    }
    private RoomSpecification(String roomType, int capacity, String description, Integer id, IDataSource dataSource) {
        super(id, dataSource);
        this.roomType = roomType;
        this.capacity = capacity;
        this.description = description;
        this.features = new HashMap<>();
    }

    private RoomSpecification(String roomType, int capacity, String description, Map<String, Feature> features, Integer id, IDataSource dataSource) {
        super(id, dataSource);
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
