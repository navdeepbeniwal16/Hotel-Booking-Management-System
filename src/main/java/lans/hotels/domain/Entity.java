package lans.hotels.domain;

public abstract class Entity implements IDomainObject {
    protected int uid;

    protected Entity() {
        this.uid = -1;
    }
    protected Entity(int id) {
        this.uid = id;
    }

    public int getUid() {
        return uid;
    }
    public boolean is(Entity otherEntity) {
        return this.uid == otherEntity.getUid();
    }
}
