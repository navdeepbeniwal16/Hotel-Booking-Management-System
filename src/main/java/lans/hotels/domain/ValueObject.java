package lans.hotels.domain;

public abstract class ValueObject implements AbstractDomainObject<Integer> {
    public static final Integer NOT_SET = null;
    Integer id;
    boolean isNew;

    public ValueObject() {
        this.id = NOT_SET;
        this.isNew = true;
    }
    public ValueObject(Integer id) {
        this.id = id;
        this.isNew = false;
    }
    public abstract boolean equals(Object other);
    public Integer getId() {
        return this.id;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }
}
