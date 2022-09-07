package lans.hotels.datasource.mappers;


import java.util.HashMap;
import java.util.Map;

import java.sql.Connection;
public abstract class AbstractMapper {
    protected Connection connection;
    protected Map loadedMap = new HashMap();
    abstract protected String findStatement();

//    protected AbstractMapper(Connection connection) {
//        this.connection = connection;
//    }
}
