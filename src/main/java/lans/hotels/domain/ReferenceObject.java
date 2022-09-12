package lans.hotels.domain;


import lans.hotels.domain.exceptions.ReferenceObjectException;

public abstract class ReferenceObject extends AbstractDomainObject<Integer> {
    public static final Integer NOT_SET = null;
    protected Integer uid;
    private boolean isNew;

    protected ReferenceObject() {
        this.id = NOT_SET;
        this.isNew = true;
    }
    protected ReferenceObject(Integer id) {
        try {
            setId(id);

        }  catch (ReferenceObjectException ignored) {}
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) throws ReferenceObjectException {
        if (this.id == NOT_SET) {
            this.id = id;
        } else {
            throw new ReferenceObjectException("cannot change UID of existing reference object");
        }
        this.isNew = false;
    }

    @Override
    public boolean equals(Object other) {
        if (other.getClass() != this.getClass()) return false;
        return this.uid == ((ReferenceObject) other).getId();
    }

}
