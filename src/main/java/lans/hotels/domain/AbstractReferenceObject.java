package lans.hotels.domain;


public abstract class AbstractReferenceObject implements IReferenceObject<Integer> {
    protected Integer uid;

    protected AbstractReferenceObject() {
        this.uid = -1;
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
