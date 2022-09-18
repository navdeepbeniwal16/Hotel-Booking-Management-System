package lans.hotels.datasource.identity_maps;

import lans.hotels.datasource.facade.IIdentityMap;
import lans.hotels.domain.AbstractDomainObject;
import lans.hotels.domain.IDataSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class IntegerIdentityMapRegistry extends AbstractIdentityMapRegistry<Integer> {
    public static IntegerIdentityMapRegistry newInstance(IDataSource<Integer> dataSource) {
        return new IntegerIdentityMapRegistry(new HashMap<>(), dataSource);
    }

    private IntegerIdentityMapRegistry(Map<String,
            IIdentityMap<Integer, ? extends AbstractDomainObject>> identityMaps,
                                       IDataSource<Integer> dataSource) {
        super(identityMaps, dataSource);
    }

    @Override
    public ArrayList<IIdentityMap<Integer, ? extends AbstractDomainObject>> getAll() {
        return new ArrayList<>(identityMaps.values());
    }
}
