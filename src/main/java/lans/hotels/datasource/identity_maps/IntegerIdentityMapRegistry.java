package lans.hotels.datasource.identity_maps;

import lans.hotels.datasource.facade.IIdentityMap;
import lans.hotels.domain.AbstractDomainObject;

import java.util.HashMap;
import java.util.Map;

public class IntegerIdentityMapRegistry extends AbstractIdentityMapRegistry<Integer> {
    public static IntegerIdentityMapRegistry newInstance() {
        return new IntegerIdentityMapRegistry(new HashMap<>());
    }

    private IntegerIdentityMapRegistry(Map<String, IIdentityMap<Integer, AbstractDomainObject>> identityMaps) {
        super(identityMaps);
    }
}
