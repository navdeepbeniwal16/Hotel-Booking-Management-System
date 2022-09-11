package lans.hotels.domain;

public abstract class ValueObject implements IDomainObject<Integer> {
    public static final Integer NOT_SET = null;
    Integer id;

    public ValueObject() {
        this.id = NOT_SET;
    }
    public ValueObject(Integer id) {
        this.id = id;
    }
    public abstract boolean equals(Object other);
    public Integer getId() {
        return this.id;
    }
}
