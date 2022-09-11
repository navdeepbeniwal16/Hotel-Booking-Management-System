package lans.hotels.domain;


public abstract class AbstractReferenceObject implements IReferenceObject<Integer> {
    public static final Integer NOT_SET = -1;
    protected Integer uid;

    protected AbstractReferenceObject() {
        this.uid = NOT_SET;
    }
    protected AbstractReferenceObject(int id) {
        this.uid = id;
    }

    public Integer getUid() {
        return uid;
    }

    @Override
    public boolean equals(Object other) {
        if (other.getClass() != this.getClass()) return false;
        return this.uid == ((AbstractReferenceObject) other).getUid();
    }
}
