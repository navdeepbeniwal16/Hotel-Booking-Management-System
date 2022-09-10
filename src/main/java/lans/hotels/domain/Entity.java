package lans.hotels.domain;

public abstract class Entity implements IDomainObject {
    protected int id;

    protected Entity() {
        this.id = -1;
    }
    protected Entity(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public boolean is(Entity otherEntity) {
        return this.id == otherEntity.getId();
    }
}
