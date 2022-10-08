package lans.hotels.domain;

import lans.hotels.datasource.exceptions.UoWException;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class AbstractDomainObject implements IGhost, Comparable<AbstractDomainObject> {
    protected IDataSource dataSource;
    protected Integer hashCode;
    protected Integer id;
    protected Boolean isNew;

    private LoadStatus loadStatus;

    public AbstractDomainObject(IDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Integer getId() {
        return this.id;
    }

    public boolean hasId() {
        return id != null;
    }

    @Override
    public boolean equals(Object other) {
        if (other.getClass() != this.getClass()) return false;
        return this.id == ((ReferenceObject) other).getId();
    }

    protected AbstractDomainObject(Boolean isNew, IDataSource dataSource) {
        this.dataSource = dataSource;
        this.isNew = isNew;
        hashCode = Objects.hash(LocalDateTime.now(), this.getClass());
    }

    protected AbstractDomainObject(Integer id, IDataSource dataSource) {
        this.id = id;
        this.dataSource = dataSource;
        this.isNew = false;
        hashCode = Objects.hash(this.id, this.getClass());
    }

     protected abstract void setId(Integer id) throws Exception;

    @Override
    public int hashCode() {
        return hashCode;
    }

    protected void load() {
        if (isGhost()) {
            dataSource.load(this); // TODO: #bug #how?
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

    // TODO: #lazyload markGhost?
    protected void markGhost() {
        assert loadStatus == null;
        loadStatus = LoadStatus.GHOST;
    }
    protected void markLoaded() {
        assert loadStatus == LoadStatus.LOADING;
        loadStatus = LoadStatus.LOADED;
    }

    protected void markLoading() {
        assert loadStatus == LoadStatus.GHOST;
        loadStatus = LoadStatus.LOADING;
    }

    // TODO: #ghost #lazyload how do these UoW commands interact with Ghost Lazy Load?
    protected void markNew() throws UoWException { dataSource.registerNew(this); }
    protected void markClean() throws UoWException { dataSource.registerClean(this); }
    protected void markDirty() throws UoWException { dataSource.registerDirty(this); }
    protected void markRemoved() throws UoWException  { dataSource.registerRemoved(this); }


    public int compareTo(AbstractDomainObject other) {
        if (!hasId() && !other.hasId()) return 0;
        if (hasId() && !other.hasId()) return 1;
        if (!hasId() && other.hasId()) return -1;
        return (id - other.id > 0) ? 1 : -1;
    }
}
