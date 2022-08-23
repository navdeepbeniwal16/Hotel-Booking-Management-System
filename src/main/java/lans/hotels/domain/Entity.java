package lans.hotels.domain;

import java.util.UUID;

public abstract class Entity {
    protected UUID uuid;
    public abstract UUID getUuid();
    public boolean is(Entity otherEntity) {
        return uuid.compareTo(otherEntity.getUuid()) == 0;
    }
}
