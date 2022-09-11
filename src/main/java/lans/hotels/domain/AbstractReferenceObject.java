package lans.hotels.domain;


import lans.hotels.domain.exceptions.ReferenceObjectException;

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

    public void setUid(int id) throws ReferenceObjectException{
        if (this.uid == NOT_SET) {
            this.uid = id;
        } else {
            throw new ReferenceObjectException("cannot change UID of existing reference object");
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other.getClass() != this.getClass()) return false;
        return this.uid == ((AbstractReferenceObject) other).getUid();
    }
}
