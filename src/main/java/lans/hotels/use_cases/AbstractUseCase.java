package lans.hotels.use_cases;

import lans.hotels.domain.AbstractDomainObject;

import java.util.List;

public abstract class AbstractUseCase {
    public abstract List<AbstractDomainObject> execute();
}
