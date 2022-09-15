package lans.hotels.domain;

import java.util.Objects;

public abstract class AbstractDomainObject<Id> implements IGhost {
    private IDataSource dataSource;
    private Integer hashCode;
    protected Id id;
    protected Boolean isNew;

    private LoadStatus loadStatus;

    public Id getId() {
        return this.id;
    }
    public Boolean isNew() {
        return isNew;
    }

    public boolean hasId() {
        return id != null;
    }

    public abstract boolean equals(Object other);

    protected AbstractDomainObject(IDataSource dataSource, Boolean isNew) {
        this.dataSource = dataSource;
        this.isNew = isNew;
    }

    @Override
    public int hashCode() {
        if (this.hashCode == null) this.hashCode = Objects.hash(this.id, this.getClass());
        return this.hashCode;
    }

    protected void load() {
        if (isGhost()) {
            dataSource.load(this);
        }
    }

    public Boolean isGhost() {
        return loadStatus == LoadStatus.GHOST;
    }

    public Boolean isLoaded() {
        return loadStatus == LoadStatus.LOADED;
    }

    public Boolean isLoading() {
        return loadStatus == LoadStatus.LOADING;
    }

    protected void markLoaded() {
        assert loadStatus == LoadStatus.LOADING;
        loadStatus = LoadStatus.LOADED;
    }

    protected void markLoading() {
        assert loadStatus == LoadStatus.GHOST;
        loadStatus = LoadStatus.LOADING;
    }
}
