package lans.hotels.domain.user_types;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.domain.IDataSource;
import lans.hotels.domain.ReferenceObject;

public class Hotelier extends ReferenceObject {

    int id;
    int userID;
    boolean isActive;

    public Hotelier(IDataSource dataSource) throws UoWException {
        super(dataSource);
        initMaps();
        markNew();
    }

    public Hotelier(Integer id, IDataSource dataSource) throws UoWException {
        super(id, dataSource);
        initMaps();
        markClean();
    }

    public Hotelier(Integer id, IDataSource dataSource, int userID, boolean isActive) {
        super(id, dataSource);
        this.isActive = isActive;
        this.userID = userID;
        initMaps();
    }

    public Boolean getStatus()
    {
        return this.isActive;
    }

    public void setStatus() throws UoWException {
        this.isActive = isActive;
        markDirty();
    }

    public int getUserID()
    {
        return this.userID;
    }

    private void initMaps() {
    }

    public int getID()
    {
        return this.id;
    }

}
