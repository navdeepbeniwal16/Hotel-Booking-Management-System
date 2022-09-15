package lans.hotels.domain;


import lans.hotels.domain.exceptions.DomainObjectException;
import lans.hotels.domain.exceptions.ReferenceObjectException;

import java.util.Objects;

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
            hashCode = Objects.hash(this.id, this.getClass());
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
