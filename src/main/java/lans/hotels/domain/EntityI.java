package lans.hotels.domain;

import java.util.UUID;

public interface EntityI {
    UUID getUUID();
    boolean isSameEntity(EntityI otherEntity);
}
