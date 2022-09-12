package lans.hotels.datasource.identity_maps;

import lans.hotels.domain.AbstractDomainObject;

import java.util.Map;

public class IntegerIdentityMapRegistry extends AbstractIdentityMapRegistry<Integer> {
    public IntegerIdentityMapRegistry(Map<String, IIdentityMap<Integer, AbstractDomainObject>> identityMaps) {
        super(identityMaps);
    }
}
