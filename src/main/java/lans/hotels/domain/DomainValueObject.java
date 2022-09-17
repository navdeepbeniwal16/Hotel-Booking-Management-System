package lans.hotels.domain;

import lans.hotels.domain.exceptions.ValueObjectException;

import java.util.Objects;

public abstract class DomainValueObject<Id> extends AbstractDomainObject<Id> implements IValueObject {
    protected DomainValueObject(IDataSource dataSource) {
        super(true, dataSource);
    }

    protected DomainValueObject(Id id, IDataSource dataSource) {
        super(id, dataSource);
    }

    public void setId(Id id) throws ValueObjectException {
        // TODO: this is rubbish - rethink approach
        if (!hasId()) {
            this.id = id;
            hashCode = Objects.hash(this.id, this.getClass());
        } else {
            throw new ValueObjectException("cannot change ID - create a new one instead");
        }
        this.isNew = false;
    }
}
