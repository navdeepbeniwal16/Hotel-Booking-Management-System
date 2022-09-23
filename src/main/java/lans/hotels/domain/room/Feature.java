package lans.hotels.domain.room;

import lans.hotels.datasource.exceptions.UoWException;
import lans.hotels.domain.DomainValueObject;
import lans.hotels.domain.IDataSource;


public class Feature extends DomainValueObject {
    String name;
    String description;

    public Feature(IDataSource dataSource) throws UoWException {
        super(dataSource);
        initMaps();
        markNew();
    }

    public Feature(Integer id, IDataSource dataSource) throws UoWException {
        super(id, dataSource);
        initMaps();
        markClean();
    }

    public Feature(String name, String description, IDataSource dataSource) throws UoWException {
        super(dataSource);
        this.name = name;
        this.description = description;
        initMaps();
    }

    public Feature(String name, String description, Integer id, IDataSource dataSource) {
        super(id, dataSource);
        this.name = name;
        this.description = description;
        initMaps();
    }

    private void initMaps() {
    }

    public String getName() { return this.name; }
    public String getDescription() { return this.description; }

    public void setName(String name) throws UoWException {
        this.name = name;
        markDirty();
    }

    public void setDescription(String description) throws UoWException {
        this.description = description;
        markDirty();
    }


}
