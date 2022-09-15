package lans.hotels.domain;


import lans.hotels.domain.exceptions.ReferenceObjectException;

public abstract class ReferenceObject extends AbstractDomainObject<Integer> {
    protected ReferenceObject(IDataSource dataSource) {
        super(dataSource, true);
        this.isNew = true;
    }
    protected ReferenceObject(IDataSource dataSource, Integer id) {
        super(dataSource, false);
        try {
            setId(id);
            this.isNew = false;

        }  catch (ReferenceObjectException ignored) {}
    }

    public void setId(Integer id) throws ReferenceObjectException {
        if (!hasId()) {
            this.id = id;
        } else {
            throw new ReferenceObjectException("cannot change UID of existing reference object");
        }
        this.isNew = false;
    }

    @Override
    public boolean equals(Object other) {
        if (other.getClass() != this.getClass()) return false;
        return this.id == ((ReferenceObject) other).getId();
    }
}
