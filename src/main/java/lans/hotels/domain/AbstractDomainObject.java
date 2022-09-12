package lans.hotels.domain;

import java.util.Objects;

public abstract class AbstractDomainObject<Id> {
    private IDataSource dataSource;
    private Integer hashCode;
    protected Id id;
    public abstract Id getId();
    public abstract void setId(Id id) throws Exception;

    protected abstract boolean isNew();
    public abstract boolean equals(Object other);

    protected AbstractDomainObject(IDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int hashCode() {
        if (this.hashCode == null) this.hashCode = Objects.hash(this.id, this.getClass());
        return this.hashCode;
    }

//    protected void load() {
//        if (isGhost())
//    }
}
