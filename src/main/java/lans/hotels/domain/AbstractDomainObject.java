package lans.hotels.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class AbstractDomainObject<Id> implements IGhost {
    protected IDataSource dataSource;
    protected Integer hashCode;
    protected Id id;
    protected Boolean isNew;

    private LoadStatus loadStatus;

    public Id getId() {
        return this.id;
    }

    public boolean hasId() {
        return id != null;
    }

    public abstract boolean equals(Object other);

    protected AbstractDomainObject(Boolean isNew, IDataSource dataSource) {
        this.dataSource = dataSource;
        this.isNew = isNew;
        hashCode = Objects.hash(LocalDateTime.now(), this.getClass());
    }

    protected AbstractDomainObject(Id id, IDataSource dataSource) {
        this.id = id;
        this.dataSource = dataSource;
        this.isNew = false;
        hashCode = Objects.hash(this.id, this.getClass());
    }

    @Override
    public int hashCode() {
        return hashCode;
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
