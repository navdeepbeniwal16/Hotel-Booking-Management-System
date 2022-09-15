package lans.hotels.domain;


import lans.hotels.domain.exceptions.ReferenceObjectException;

public abstract class ReferenceObject extends AbstractDomainObject<Integer> {
    protected ReferenceObject(IDataSource dataSource) {
        super(true, dataSource);
    }
    protected ReferenceObject(Integer id, IDataSource dataSource) {
        super(id, dataSource);
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
