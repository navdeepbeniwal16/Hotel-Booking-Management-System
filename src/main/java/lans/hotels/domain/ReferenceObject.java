package lans.hotels.domain;


import lans.hotels.domain.exceptions.ReferenceObjectException;

public abstract class ReferenceObject implements IDomainObject<Integer> {
    public static final Integer NOT_SET = null;
    protected Integer uid;
    private boolean isNew;

    protected ReferenceObject() {
        this.uid = NOT_SET;
        this.isNew = true;
    }
    protected ReferenceObject(int id) {
        try { setUid(id); }  catch (ReferenceObjectException ignored) {}
    }

    public Integer getId() {
        return getUid();
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
        return this.uid == ((ReferenceObject) other).getUid();
    }
}
