package lans.hotels.domain;


import lans.hotels.domain.exceptions.ReferenceObjectException;

public abstract class DomainObject implements IReferenceObject<Integer> {
    public static final Integer NOT_SET = null;
    protected Integer uid;
    private boolean isNew;

    protected DomainObject() {
        this.uid = NOT_SET;
        this.isNew = true;
    }
    protected DomainObject(int id) {
        try { setUid(id); }  catch (ReferenceObjectException ignored) {}
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
        this.isNew = false;
    }

    @Override
    public boolean equals(Object other) {
        if (other.getClass() != this.getClass()) return false;
        return this.uid == ((DomainObject) other).getUid();
    }
}
