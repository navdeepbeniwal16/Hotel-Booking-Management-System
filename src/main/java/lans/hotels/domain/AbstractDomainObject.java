package lans.hotels.domain;

import lans.hotels.datasource.exceptions.DataSourceLayerException;
import lans.hotels.domain.exceptions.DomainObjectException;
import lans.hotels.domain.exceptions.ReferenceObjectException;
import org.json.JSONObject;

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

     abstract void setId(Id id) throws Exception;

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
    protected void markLoaded() {
        assert loadStatus == LoadStatus.LOADING;
        loadStatus = LoadStatus.LOADED;
    }

    protected void markLoading() {
        assert loadStatus == LoadStatus.GHOST;
        loadStatus = LoadStatus.LOADING;
    }

    // TODO: #ghost #lazyload how do these UoW commands interact with Ghost Lazy Load?
    protected void markNew() throws Exception  { dataSource.registerNew(this); }
    protected void markClean() throws Exception { dataSource.registerClean(this); }
    protected void markDirty() throws Exception { dataSource.registerDirty(this); }
    protected void markRemoved() throws Exception  { dataSource.registerRemoved(this); }
}
